package de.dbaelz.pnp.logbook.features.actionlog

import de.dbaelz.pnp.logbook.HEADER_X_PLATFORM
import de.dbaelz.pnp.logbook.Platform
import de.dbaelz.pnp.logbook.features.ApiRoute
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.*

class ActionLog(val configuration: Configuration) {
    class Configuration {
        var repository: ActionLogRepository? = null
        var headerName = HEADER_X_PLATFORM
    }

    companion object Plugin : BaseApplicationPlugin<ApplicationCallPipeline, Configuration, ActionLog> {
        override val key = AttributeKey<ActionLog>("ActionLog")

        override fun install(
            pipeline: ApplicationCallPipeline,
            configure: Configuration.() -> Unit
        ): ActionLog {
            val configuration = Configuration()
            configure(configuration)

            val plugin = ActionLog(configuration)

            pipeline.intercept(ApplicationCallPipeline.Monitoring) {
                if (call.request.uri.contains(ApiRoute.ACTION_LOG.fullResourcePath)) return@intercept

                call.request.headers[configuration.headerName]?.let { platformHeader ->
                    val platform = Platform.entries
                        .find { it.name == platformHeader } ?: Platform.Unknown

                    plugin.configuration.repository?.add(
                        item = ActionLogItem(
                            platform = platform,
                            httpMethod = call.request.httpMethod.value,
                            path = call.request.path()
                        )
                    ) ?: error("No repository configured")
                }
            }

            return plugin
        }
    }
}