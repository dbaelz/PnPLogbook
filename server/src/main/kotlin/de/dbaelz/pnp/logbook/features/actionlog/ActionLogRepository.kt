package de.dbaelz.pnp.logbook.features.actionlog

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ActionLogRepository {
    private val _actionLog = MutableSharedFlow<ActionLogItem>(
        replay = 20
    )
    val actionLog = _actionLog.asSharedFlow()

    fun add(item: ActionLogItem) {
        _actionLog.tryEmit(item)
    }
}