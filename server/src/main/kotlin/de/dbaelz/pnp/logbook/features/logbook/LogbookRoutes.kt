package de.dbaelz.pnp.logbook.features.logbook

import de.dbaelz.pnp.logbook.features.logbook.AddLogbookEntry
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerLogbookRoutes() {
    val logbookRepository = LogbookRepository()

    route("/logbook") {
        get {
            val logbook = logbookRepository.getLogbook()

            call.respond(logbook)
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val logbookEntry = logbookRepository.getLogbookEntry(id)
            if (logbookEntry == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            call.respond(logbookEntry)
        }

        post {
            try {
                val addLogbookEntry = call.receive<AddLogbookEntry>()
                logbookRepository.add(addLogbookEntry)
                call.respond(HttpStatusCode.NoContent)
            } catch (exception: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (exception: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}