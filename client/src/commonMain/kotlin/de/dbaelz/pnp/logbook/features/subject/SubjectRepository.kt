package de.dbaelz.pnp.logbook.features.subject

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class SubjectRepository(
    private val httpClient: HttpClient,
    private val resource: String
) {
    suspend fun getSubjectEntries(): List<Subject> {
        return httpClient.get(resource).body()
    }

    suspend fun addSubject(
        name: String,
        description: String,
        notes: String
    ): List<Subject> {
        return httpClient.post(resource) {
            contentType(ContentType.Application.Json)
            setBody(AddSubject(name, description, notes))
        }.body()
    }
}