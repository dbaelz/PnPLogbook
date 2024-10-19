package de.dbaelz.pnp.logbook.features.experience

object ExperienceViewModelContract {
    sealed interface State {
        data object Loading : State

        data class Content(
            val isLoading: Boolean = false,
            val total: Int = 0,
            val experienceEntries: List<Experience> = emptyList(),
            val message: String? = null
        ) : State
    }

    sealed interface Event {
        data class AddExperience(
            val experience: Int,
            val reason: String
        ) : Event
    }
}