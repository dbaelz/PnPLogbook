package de.dbaelz.pnp.logbook.features.experience

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class ExperienceRepository(private val httpClient: HttpClient) {
    suspend fun getExperience(): ExperienceDTO {
        return httpClient.get("experience").body()
    }

    suspend fun addExperience(experience: Int, reason: String): ExperienceDTO {
        return httpClient.post("experience") {
            contentType(ContentType.Application.Json)
            setBody(AddExperience(experience, reason))
        }.body()
    }
}