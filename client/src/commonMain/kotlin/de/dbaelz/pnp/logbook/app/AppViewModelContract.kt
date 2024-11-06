package de.dbaelz.pnp.logbook.app

import de.dbaelz.pnp.logbook.features.actionlog.ActionLogItem

object AppViewModelContract {
    data class State(
        val actionLogItems: List<ActionLogItem> = emptyList()
    )

    sealed interface Event {
        data object ShowActionLog : Event

        data object ShutdownServer : Event
    }
}