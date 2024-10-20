package de.dbaelz.pnp.logbook.network

import de.dbaelz.pnp.logbook.SERVER_PORT
import de.dbaelz.pnp.logbook.getServerHost
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

val httpClient = HttpClient {
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
    defaultRequest {
        url {
            host = getServerHost()
            port = SERVER_PORT
            path("api/")
        }
    }
}