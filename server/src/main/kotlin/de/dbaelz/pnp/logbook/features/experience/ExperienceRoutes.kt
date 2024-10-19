package de.dbaelz.pnp.logbook.features.experience

import de.dbaelz.pnp.logbook.features.experience.AddExperience
import de.dbaelz.pnp.logbook.features.experience.ExperienceDTO
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerExperienceRoutes() {
    val experienceRepository = ExperienceRepository()

    route("/experience") {
        get {
            val experience = experienceRepository.getExperience()

            call.respond(
                ExperienceDTO(
                    total = experience.sumOf { it.experience },
                    entries = experience
                )
            )
        }

        post {
            try {
                val addExperience = call.receive<AddExperience>()
                experienceRepository.add(addExperience)
                call.respond(HttpStatusCode.NoContent)
            } catch (exception: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (exception: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}