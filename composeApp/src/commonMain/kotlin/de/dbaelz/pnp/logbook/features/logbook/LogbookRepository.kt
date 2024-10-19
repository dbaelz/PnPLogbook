package de.dbaelz.pnp.logbook.features.logbook

import de.dbaelz.pnp.logbook.network.httpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class LogbookRepository {
    suspend fun getLogbook(): List<LogbookEntry> {
        return httpClient.get("logbook").body()
    }

    suspend fun addLogbookEntry(location: String, text: String): List<LogbookEntry> {
        return httpClient.post("logbook") {
            contentType(ContentType.Application.Json)
            setBody(AddLogbookEntry(location, text))
        }.body()
    }
}