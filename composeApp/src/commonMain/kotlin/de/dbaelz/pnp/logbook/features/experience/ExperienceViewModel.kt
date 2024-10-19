package de.dbaelz.pnp.logbook.features.experience

import androidx.lifecycle.viewModelScope
import de.dbaelz.pnp.logbook.features.experience.ExperienceDTO
import de.dbaelz.pnp.logbook.features.experience.ExperienceViewModel.Internal
import de.dbaelz.pnp.logbook.features.experience.ExperienceViewModelContract.Event
import de.dbaelz.pnp.logbook.features.experience.ExperienceViewModelContract.State
import de.dbaelz.pnp.logbook.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ExperienceViewModel(
    private val experienceRepository: ExperienceRepository
) : BaseViewModel<State, Event, Internal.Event>() {
    override val initialState: State
        get() = State.Loading

    init {
        viewModelScope.launch {
            event.map { reduce(state.value, it) }.collect { updateState(it) }
        }

        viewModelScope.launch {
            getExperience()
        }
    }

    private fun reduce(state: State, event: Event): State {
        return when (event) {
            is Internal.Event -> reduceInternal(state, event)

            is Event.AddExperience -> {
                addExperience(event.experience, event.reason)
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
            is Internal.Event.UpdateExperience -> {
                State.Content(
                    isLoading = false,
                    total = event.experience.total,
                    experienceEntries = event.experience.entries
                )
            }

            is Internal.Event.ShowMessage -> {
                State.Content(
                    message = event.message
                )
            }
        }
    }

    private fun getExperience() {
        viewModelScope.launch {
            try {
                val experience = experienceRepository.getExperience()
                sendEvent(Internal.Event.UpdateExperience(experience))
            } catch (exception: Exception) {
                sendEvent(Internal.Event.ShowMessage("Error loading experience: ${exception.message}"))
            }
        }
    }

    private fun addExperience(experience: Int, reason: String) {
        viewModelScope.launch {
            try {
                val updatedExperience = experienceRepository.addExperience(experience, reason)
                sendEvent(Internal.Event.UpdateExperience(updatedExperience))
            } catch (exception: Exception) {
                sendEvent(Internal.Event.ShowMessage("Error adding experience: ${exception.message}"))
            }
        }
    }

    object Internal {
        sealed interface Event : ExperienceViewModelContract.Event {
            data class UpdateExperience(val experience: ExperienceDTO) : Event

            data class ShowMessage(val message: String) : Event
        }
    }
}