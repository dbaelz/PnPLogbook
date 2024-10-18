package de.dbaelz.pnp.logbook

import de.dbaelz.pnp.logbook.currency.registerCurrencyRoutes
import de.dbaelz.pnp.logbook.experience.registerExperienceRoutes
import de.dbaelz.pnp.logbook.logbook.registerLogbookRoutes
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.io.File
import java.sql.Connection

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = getServerHost(), module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureDatabase()

    install(CORS) {
        // Fixme: Only for local testing
        allowHost("localhost:8081")
        allowHeader(HttpHeaders.ContentType)
    }

    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Welcome to the PnP Logbook!")
        }

        registerExperienceRoutes()
        registerCurrencyRoutes()
        registerLogbookRoutes()
    }
}

private fun configureDatabase() {
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    File("./data").mkdir()
    Database.connect("jdbc:sqlite:data/data.db", "org.sqlite.JDBC")
}