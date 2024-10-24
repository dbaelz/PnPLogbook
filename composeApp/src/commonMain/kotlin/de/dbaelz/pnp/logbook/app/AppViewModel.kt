package de.dbaelz.pnp.logbook.app

import androidx.lifecycle.viewModelScope
import de.dbaelz.pnp.logbook.SERVER_PORT
import de.dbaelz.pnp.logbook.app.AppViewModel.Internal
import de.dbaelz.pnp.logbook.app.AppViewModelContract.Event
import de.dbaelz.pnp.logbook.app.AppViewModelContract.State
import de.dbaelz.pnp.logbook.features.actionlog.ActionLogItem
import de.dbaelz.pnp.logbook.getServerHost
import de.dbaelz.pnp.logbook.viewmodel.BaseViewModel
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.plugins.sse.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class AppViewModel(
    private val httpClient: HttpClient
) : BaseViewModel<State, Event, Internal.Event>() {
    override val initialState: State
        get() = State()

    private var sseJob: Job? = null

    init {
        viewModelScope.launch {
            event.map { reduce(state.value, it) }.collect { updateState(it) }
        }

        viewModelScope.launch {
            observeSSE()
        }
    }

    private fun reduce(state: State, event: Event): State {
        return when (event) {
            is Internal.Event -> reduceInternal(state, event)

            is Event.ShowActionLog -> state
        }
    }

    private fun reduceInternal(state: State, event: Internal.Event): State {
        return when (event) {
            is Internal.Event.AddActionLogItem -> {
                state.copy(
                    actionLogItems = state.actionLogItems + listOf(event.item)
                )
            }
        }
    }

    private fun observeSSE() {
        sseJob?.cancel()
        sseJob = viewModelScope.launch {
            httpClient.sse(
                host = getServerHost(),
                port = SERVER_PORT,
                path = "api/actionlog"
            ) {
                incoming.collect { event ->
                    event.data?.let {
                        try {
                            sendEvent(
                                Internal.Event.AddActionLogItem(
                                    Json.decodeFromString<ActionLogItem>(it)
                                )
                            )
                        } catch (exception: Exception) {
                            Napier.e(
                                throwable = exception,
                                message = "Error parsing SSE event: $it"
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        httpClient.close()
        sseJob?.cancel()
    }

    object Internal {
        sealed interface Event : AppViewModelContract.Event {
            data class AddActionLogItem(val item: ActionLogItem) : Event
        }
    }
}