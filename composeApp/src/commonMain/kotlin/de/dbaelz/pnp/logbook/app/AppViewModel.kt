package de.dbaelz.pnp.logbook.app

import androidx.lifecycle.viewModelScope
import de.dbaelz.pnp.logbook.SERVER_PORT
import de.dbaelz.pnp.logbook.app.AppViewModel.Internal
import de.dbaelz.pnp.logbook.app.AppViewModelContract.Event
import de.dbaelz.pnp.logbook.app.AppViewModelContract.State
import de.dbaelz.pnp.logbook.features.ApiRoute
import de.dbaelz.pnp.logbook.features.actionlog.ActionLogItem
import de.dbaelz.pnp.logbook.features.apiResource
import de.dbaelz.pnp.logbook.getServerHost
import de.dbaelz.pnp.logbook.viewmodel.BaseViewModel
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.plugins.sse.*
import io.ktor.client.request.*
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
            is Event.ShutdownServer -> {
                shutdownServer()
                state
            }
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

    private fun shutdownServer() {
        viewModelScope.launch {
            try {
                httpClient.get(ApiRoute.SHUTDOWN.resource)
            } catch (exception: Exception) {
                Napier.e("Error shutting down server", exception)
            }
        }
    }

    private fun observeSSE() {
        sseJob?.cancel()
        sseJob = viewModelScope.launch {
            try {
                httpClient.sse(
                    host = getServerHost(),
                    port = SERVER_PORT,
                    path = "$apiResource${ApiRoute.ACTION_LOG.resourcePath}"
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
            } catch (exception: Exception) {
                Napier.e("Error connecting to SSE", exception)
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