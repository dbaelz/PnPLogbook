package de.dbaelz.pnp.logbook.features.logbook

import androidx.lifecycle.viewModelScope
import de.dbaelz.pnp.logbook.features.logbook.LogbookViewModel.Internal
import de.dbaelz.pnp.logbook.features.logbook.LogbookViewModelContract.Event
import de.dbaelz.pnp.logbook.features.logbook.LogbookViewModelContract.State
import de.dbaelz.pnp.logbook.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LogbookViewModel(
    private val logbookRepository: LogbookRepository
) : BaseViewModel<State, Event, Internal.Event>() {
    override val initialState: State
        get() = State.Loading

    init {
        viewModelScope.launch {
            event.map { reduce(state.value, it) }.collect { updateState(it) }
        }

        viewModelScope.launch {
            getLogbook()
        }
    }

    private fun reduce(state: State, event: Event): State {
        return when (event) {
            is Internal.Event -> reduceInternal(state, event)

            is Event.AddLogbook -> {
                addLogbook(event.location, event.text)
                if (state is State.Content) {
                    state.copy(
                        isLoading = true
                    )
                } else {
                    State.Content(
                        isLoading = true
                    )
                }
            }
        }
    }

    private fun reduceInternal(state: State, event: Internal.Event): State {
        return when (event) {
            is Internal.Event.UpdateLogbook -> {
                State.Content(
                    isLoading = false,
                    logbookEntries = event.logbook
                )
            }

            is Internal.Event.ShowMessage -> {
                State.Content(
                    isLoading = false,
                    message = event.message
                )
            }
        }
    }

    private fun getLogbook() {
        viewModelScope.launch {
            try {
                val logbook = logbookRepository.getLogbook()
                sendEvent(Internal.Event.UpdateLogbook(logbook))
            } catch (exception: Exception) {
                sendEvent(Internal.Event.ShowMessage("Error loading logbook: ${exception.message}"))
            }
        }
    }

    private fun addLogbook(location: String, text: String) {
        viewModelScope.launch {
            try {
                val updatedLogbook = logbookRepository.addLogbookEntry(location, text)
                sendEvent(Internal.Event.UpdateLogbook(updatedLogbook))
            } catch (exception: Exception) {
                sendEvent(Internal.Event.ShowMessage("Error adding logbook: ${exception.message}"))
            }
        }
    }

    object Internal {
        sealed interface Event : LogbookViewModelContract.Event {
            data class UpdateLogbook(val logbook: List<LogbookEntry>) : Event

            data class ShowMessage(val message: String) : Event
        }
    }
}