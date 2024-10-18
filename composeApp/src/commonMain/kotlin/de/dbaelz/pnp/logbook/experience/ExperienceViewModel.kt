package de.dbaelz.pnp.logbook.experience

import androidx.lifecycle.viewModelScope
import de.dbaelz.pnp.logbook.experience.ExperienceViewModel.Internal
import de.dbaelz.pnp.logbook.experience.ExperienceViewModelContract.Event
import de.dbaelz.pnp.logbook.experience.ExperienceViewModelContract.State
import de.dbaelz.pnp.logbook.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ExperienceViewModel(
    experienceRepository: ExperienceRepository
) : BaseViewModel<State, Event, Internal.Event>() {
    override val initialState: State
        get() = State.Loading

    init {
        viewModelScope.launch {
            event.map { reduce(state.value, it) }.collect { updateState(it) }
        }

        viewModelScope.launch {
            experienceRepository.getExperience()
        }
    }

    private fun reduce(state: State, event: Event): State {
        return when (event) {
            is Internal.Event.UpdateExperience -> {
                if (state is State.Content) {
                    state.copy(
                        experience = event.experience.entries,
                        message = null
                    )
                } else {
                    State.Content(
                        experience = event.experience.entries
                    )
                }
            }

            is Event.AddExperience -> state
        }
    }

    object Internal {
        sealed interface Event: ExperienceViewModelContract.Event {
            data class UpdateExperience(val experience: ExperienceDTO): Event
        }
    }
}