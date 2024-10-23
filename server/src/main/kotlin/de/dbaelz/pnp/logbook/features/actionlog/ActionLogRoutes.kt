package de.dbaelz.pnp.logbook.features.actionlog

import io.ktor.server.routing.*
import io.ktor.server.sse.*
import io.ktor.sse.*
import kotlinx.serialization.json.Json

fun Route.registerActionLogRoutes(actionLogRepository: ActionLogRepository) {
    sse("/actionlog") {
        actionLogRepository.actionLog.collect { actionLogItem ->
            send(
                ServerSentEvent(
                    data = Json.encodeToString(ActionLogItem.serializer(), actionLogItem)
                )
            )
        }
    }
}