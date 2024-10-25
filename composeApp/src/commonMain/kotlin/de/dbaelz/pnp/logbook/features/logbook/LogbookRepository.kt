package de.dbaelz.pnp.logbook.features.logbook

import de.dbaelz.pnp.logbook.features.ApiRoute
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class LogbookRepository(private val httpClient: HttpClient) {
    suspend fun getLogbook(): List<LogbookEntry> {
        return httpClient.get(ApiRoute.LOGBOOK.resource).body()
    }

    suspend fun addLogbookEntry(location: String, text: String): List<LogbookEntry> {
        return httpClient.post(ApiRoute.LOGBOOK.resource) {
            contentType(ContentType.Application.Json)
            setBody(AddLogbookEntry(location, text))
        }.body()
    }

    suspend fun deleteLogbookEntry(id: Int): List<LogbookEntry> {
        return httpClient.delete("${ApiRoute.LOGBOOK.resource}/$id").body()
    }
}