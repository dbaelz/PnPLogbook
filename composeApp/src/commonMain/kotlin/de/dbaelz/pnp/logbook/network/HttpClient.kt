package de.dbaelz.pnp.logbook.network

import de.dbaelz.pnp.logbook.SERVER_HOST
import de.dbaelz.pnp.logbook.SERVER_PORT
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            encodeDefaults = true
            isLenient = true
            coerceInputValues = true
            ignoreUnknownKeys = true
        })
    }
    defaultRequest {
        host = SERVER_HOST
        port = SERVER_PORT
    }
}