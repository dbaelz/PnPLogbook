package de.dbaelz.pnp.logbook

import de.dbaelz.pnp.logbook.currency.registerCurrencyRoutes
import de.dbaelz.pnp.logbook.xp.registerExperienceRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Welcome to the PnP Logbook!")
        }

        registerExperienceRoutes()
        registerCurrencyRoutes()
    }
}