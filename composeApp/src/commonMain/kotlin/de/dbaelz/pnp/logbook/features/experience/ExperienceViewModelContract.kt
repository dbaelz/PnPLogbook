package de.dbaelz.pnp.logbook.features.experience

import de.dbaelz.pnp.logbook.features.experience.Experience

object ExperienceViewModelContract {
    sealed interface State {
        data object Loading : State

        data class Content(
            val total: Int = 0,
            val experienceEntries: List<Experience> = emptyList(),
            val message: String? = null
        ) : State
    }

    sealed interface Event {
        data class AddExperience(val experience: Experience) : Event
    }
}