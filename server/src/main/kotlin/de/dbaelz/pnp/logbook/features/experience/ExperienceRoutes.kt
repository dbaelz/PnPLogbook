package de.dbaelz.pnp.logbook.features.experience

import de.dbaelz.pnp.logbook.features.ApiRoute
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerExperienceRoutes(experienceRepository: ExperienceRepository) {
    route(ApiRoute.EXPERIENCE.resourcePath) {
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

                val experience = experienceRepository.getExperience()
                call.respond(
                    ExperienceDTO(
                        total = experience.sumOf { it.experience },
                        entries = experience
                    )
                )
            } catch (exception: CannotTransformContentToTypeException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (exception: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (exception: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}