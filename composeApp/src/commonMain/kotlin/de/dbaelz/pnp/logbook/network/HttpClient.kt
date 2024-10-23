package de.dbaelz.pnp.logbook.network

import de.dbaelz.pnp.logbook.HEADER_X_PLATFORM
import de.dbaelz.pnp.logbook.SERVER_PORT
import de.dbaelz.pnp.logbook.getPlatform
import de.dbaelz.pnp.logbook.getServerHost
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.sse.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json


val httpClient = createHttpClient()

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
                path("api/")
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