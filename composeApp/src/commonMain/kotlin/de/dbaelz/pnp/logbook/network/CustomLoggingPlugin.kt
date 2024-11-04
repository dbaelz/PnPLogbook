package de.dbaelz.pnp.logbook.network

import io.github.aakira.napier.Napier
import io.ktor.client.plugins.api.*
import io.ktor.client.statement.*

/**
 * Custom client plugin to log response as WARN log message based on the request path
 */
val CustomLoggingPlugin = createClientPlugin("CustomLoggingPlugin", ::CustomLoggingPluginConfig) {
    val fullResourcePath: String? = pluginConfig.fullResourcePath
    val logTag: String = pluginConfig.logTag
    val logMessage: (HttpResponse) -> String = pluginConfig.logMessage

    onResponse { response ->
        if (fullResourcePath != null && response.call.request.url.encodedPath == fullResourcePath) {
            Napier.w(
                tag = logTag,
                message = logMessage(response)
            )
        }
    }
}

class CustomLoggingPluginConfig {
    var fullResourcePath: String? = null
    var logTag: String = "CustomLoggingPlugin"
    var logMessage: (HttpResponse) -> String = { "Response: $it" }
}