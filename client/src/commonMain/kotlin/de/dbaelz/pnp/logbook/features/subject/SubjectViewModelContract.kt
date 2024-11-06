package de.dbaelz.pnp.logbook.features.subject

object SubjectViewModelContract {
    sealed interface State {
        data object Loading : State

        data class Content(
            val isLoading: Boolean = false,
            val subjectEntries: List<Subject> = emptyList(),
            val message: String? = null
        ) : State
    }

    sealed interface Event {
        data class AddSubject(
            val name: String,
            val description: String,
            val notes: String
        ) : Event
    }
}