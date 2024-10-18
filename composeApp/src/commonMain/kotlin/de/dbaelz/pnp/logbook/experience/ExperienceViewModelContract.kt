package de.dbaelz.pnp.logbook.experience

object ExperienceViewModelContract {
    sealed interface State {
        data object Loading : State

        data class Content(
            val experience: List<Experience> = emptyList(),
            val message: String? = null
        ) : State
    }

    sealed interface Event {
        data class AddExperience(val experience: Experience) : Event
    }
}