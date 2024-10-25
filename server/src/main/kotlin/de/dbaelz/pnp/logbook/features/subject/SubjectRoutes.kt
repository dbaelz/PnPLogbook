package de.dbaelz.pnp.logbook.features.subject

import de.dbaelz.pnp.logbook.features.ApiRoute
import de.dbaelz.pnp.logbook.features.subject.SubjectRepository.SubjectType
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerSubjectRoutes() {
    val subjectRepository = SubjectRepository()

    listOf(
        ApiRoute.PERSONS.resourcePath to SubjectType.Person,
        ApiRoute.GROUPS.resourcePath to SubjectType.Group,
        ApiRoute.PLACES.resourcePath to SubjectType.Place,
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

