package de.dbaelz.pnp.logbook.features.logbook

object LogbookViewModelContract {
    sealed interface State {
        data object Loading : State

        data class Content(
            val isLoading: Boolean = false,
            val logbookEntries: List<LogbookEntry> = emptyList(),
            val message: String? = null
        ) : State
    }

    sealed interface Event {
        data class AddLogbook(
            val location: String,
            val text: String
        ) : Event
    }
}