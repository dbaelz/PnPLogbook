package de.dbaelz.pnp.logbook.features.subject

import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerSubjectRoutes() {
    val subjectRepository = SubjectRepository()

    listOf(
        "/persons" to SubjectRepository.SubjectType.Person,
        "/groups" to SubjectRepository.SubjectType.Group,
        "/places" to SubjectRepository.SubjectType.Place,
    ).forEach { (path, subjectType) ->
        route(path) {
            get {
                call.respond(subjectRepository.getByType(subjectType))
            }

            post {
                try {
                    val addSubject = call.receive<AddSubject>()
                    subjectRepository.add(addSubject, subjectType)

                    call.respond(subjectRepository.getByType(subjectType))
                } catch (exception: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (exception: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            put("/{id}") {
                try {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw IllegalArgumentException("Invalid id")
                    val updatedSubject = call.receive<AddSubject>()
                    subjectRepository.update(id, updatedSubject)
                    call.respond(HttpStatusCode.NoContent)
                } catch (exception: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (exception: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (exception: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

        }
    }
}

