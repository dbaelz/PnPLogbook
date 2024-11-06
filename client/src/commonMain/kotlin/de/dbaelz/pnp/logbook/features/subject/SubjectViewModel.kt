package de.dbaelz.pnp.logbook.features.subject

import androidx.lifecycle.viewModelScope
import de.dbaelz.pnp.logbook.features.subject.SubjectViewModel.Internal
import de.dbaelz.pnp.logbook.features.subject.SubjectViewModelContract.Event
import de.dbaelz.pnp.logbook.features.subject.SubjectViewModelContract.State
import de.dbaelz.pnp.logbook.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SubjectViewModel(
    private val subjectRepository: SubjectRepository
) : BaseViewModel<State, Event, Internal.Event>() {
    override val initialState: State
        get() = State.Loading

    init {
        viewModelScope.launch {
            event.map { reduce(state.value, it) }.collect { updateState(it) }
        }

        viewModelScope.launch {
            getSubjectEntries()
        }
    }

    private fun reduce(state: State, event: Event): State {
        return when (event) {
            is Internal.Event -> reduceInternal(state, event)

            is Event.AddSubject -> {
                addSubject(event.name, event.description, event.notes)
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
            is Internal.Event.UpdateSubjectEntries -> {
                State.Content(
                    isLoading = false,
                    subjectEntries = event.subjectEntries
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

    private fun getSubjectEntries() {
        viewModelScope.launch {
            try {
                val subject = subjectRepository.getSubjectEntries()
                sendEvent(Internal.Event.UpdateSubjectEntries(subject))
            } catch (exception: Exception) {
                sendEvent(Internal.Event.ShowMessage("Error loading subject: ${exception.message}"))
            }
        }
    }

    private fun addSubject(name: String, description: String, notes: String) {
        viewModelScope.launch {
            try {
                val updatedSubject = subjectRepository.addSubject(name, description, notes)
                sendEvent(Internal.Event.UpdateSubjectEntries(updatedSubject))
            } catch (exception: Exception) {
                sendEvent(Internal.Event.ShowMessage("Error adding subject: ${exception.message}"))
            }
        }
    }

    object Internal {
        sealed interface Event : SubjectViewModelContract.Event {
            data class UpdateSubjectEntries(val subjectEntries: List<Subject>) : Event

            data class ShowMessage(val message: String) : Event
        }
    }
}