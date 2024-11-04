package de.dbaelz.pnp.logbook.features.actionlog

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

interface ActionLogRepository {
    val actionLog: SharedFlow<ActionLogItem>

    fun add(item: ActionLogItem)
}

class ActionLogRepositoryImpl : ActionLogRepository {
    private val _actionLog = MutableSharedFlow<ActionLogItem>(
        replay = 20
    )

    override val actionLog = _actionLog.asSharedFlow()

    override fun add(item: ActionLogItem) {
        _actionLog.tryEmit(item)
    }
}