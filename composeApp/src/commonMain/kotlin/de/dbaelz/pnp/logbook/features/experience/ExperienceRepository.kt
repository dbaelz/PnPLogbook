package de.dbaelz.pnp.logbook.features.experience

import de.dbaelz.pnp.logbook.experience.ExperienceDTO
import de.dbaelz.pnp.logbook.network.httpClient
import io.ktor.client.call.*
import io.ktor.client.request.*

class ExperienceRepository {
    suspend fun getExperience(): ExperienceDTO {
        return httpClient.get("experience").body()
    }
}