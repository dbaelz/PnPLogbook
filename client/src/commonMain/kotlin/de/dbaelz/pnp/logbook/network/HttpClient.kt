package de.dbaelz.pnp.logbook.network

import de.dbaelz.pnp.logbook.*
import de.dbaelz.pnp.logbook.features.ApiRoute
import de.dbaelz.pnp.logbook.features.apiResource
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.sse.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json


fun createHttpClient(engine: HttpClientEngine? = null): HttpClient {
    val clientConfig: HttpClientConfig<*>.() -> Unit = {
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.d(message = message, tag = "HttpClient")
                }
            }
            level = LogLevel.ALL
        }

        install(CustomLoggingPlugin) {
            fullResourcePath = ApiRoute.SHUTDOWN.fullResourcePath
            logTag = "Shutdown"
            logMessage = { response ->
                "ShutdownResponse received: ${response.status.value} ${response.status.description}"
            }
        }

        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(BASIC_AUTH_USERNAME, BASIC_AUTH_PASSWORD)
                }
                realm = BASIC_AUTH_REALM
            }
        }

        install(ContentEncoding) {
            gzip()
        }

        install(ContentNegotiation) {
            json(Json {
                encodeDefaults = true
                isLenient = true
                coerceInputValues = true
                ignoreUnknownKeys = true
            })
        }

        install(SSE)

        defaultRequest {
            url {
                host = getServerHost()
                port = SERVER_PORT
                path("$apiResource/")
            }
            header(HEADER_X_PLATFORM, getPlatform())
        }
    }

    return if (engine == null) {
        HttpClient(clientConfig)
    } else {
        HttpClient(engine, clientConfig)
    }
}