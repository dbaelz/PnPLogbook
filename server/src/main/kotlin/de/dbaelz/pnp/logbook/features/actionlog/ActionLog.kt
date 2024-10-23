package de.dbaelz.pnp.logbook.features.actionlog

import de.dbaelz.pnp.logbook.HEADER_X_PLATFORM
import de.dbaelz.pnp.logbook.Platform
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.*

class ActionLog(val configuration: Configuration) {
    class Configuration(val repository: ActionLogRepository) {
        var headerName = HEADER_X_PLATFORM
    }

    companion object Plugin : BaseApplicationPlugin<ApplicationCallPipeline, Configuration, ActionLog> {
        override val key = AttributeKey<ActionLog>("ActionLog")

        override fun install(
            pipeline: ApplicationCallPipeline,
            configure: Configuration.() -> Unit
        ): ActionLog {
            val configuration = Configuration(ActionLogRepository()).apply(configure)
            val plugin = ActionLog(configuration)

            pipeline.intercept(ApplicationCallPipeline.Monitoring) {
                call.request.headers[configuration.headerName]?.let { platformHeader ->
                    val platform = Platform.entries
                        .find { it.name == platformHeader } ?: Platform.Unknown

                    configuration.repository.add(
                        item = ActionLogItem(
                            platform = platform,
                            httpMethod = call.request.httpMethod.value,
                            path = call.request.path()
                        )
                    )
                }
            }

            return plugin
        }
    }
}