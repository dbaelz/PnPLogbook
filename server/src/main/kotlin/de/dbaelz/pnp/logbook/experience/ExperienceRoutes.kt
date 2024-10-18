package de.dbaelz.pnp.logbook.experience

import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.registerExperienceRoutes() {
    val experienceRepository = ExperienceRepository()

    routing {
        route("/experience") {
            get {
                val experience = experienceRepository.getExperience()

                call.respond(
                    ExperienceResponse(
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
}

@Serializable
data class ExperienceResponse(val total: Int, val entries: List<Experience>)