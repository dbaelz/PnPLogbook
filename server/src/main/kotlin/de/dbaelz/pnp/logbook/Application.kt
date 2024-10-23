package de.dbaelz.pnp.logbook

import de.dbaelz.pnp.logbook.features.actionlog.ActionLog
import de.dbaelz.pnp.logbook.features.actionlog.registerActionLogRoutes
import de.dbaelz.pnp.logbook.features.currency.registerCurrencyRoutes
import de.dbaelz.pnp.logbook.features.experience.registerExperienceRoutes
import de.dbaelz.pnp.logbook.features.logbook.registerLogbookRoutes
import de.dbaelz.pnp.logbook.features.rootHtml
import de.dbaelz.pnp.logbook.features.subject.registerSubjectRoutes
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
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
        anyHost()
        anyMethod()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HEADER_X_PLATFORM)

        // TODO: Fix CORS and make Wasm working with SSE...
    }

    install(ContentNegotiation) {
        json()
    }

    // Custom plugin that logs API Calls as action log and emits them via ActionLogRepository
    // Should inject the repository with DI in the future
    val actionLogPlugin = install(ActionLog)

    install(SSE)

    routing {
        get("/") {
            call.respondHtml(
                status = HttpStatusCode.OK, block = rootHtml()
            )
        }

        route("/api") {
            registerExperienceRoutes()
            registerCurrencyRoutes()
            registerLogbookRoutes()
            registerSubjectRoutes()

            registerActionLogRoutes(actionLogPlugin.configuration.repository)
        }
    }
}

private fun configureDatabase() {
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    File("./data").mkdir()
    Database.connect("jdbc:sqlite:data/data.db", "org.sqlite.JDBC")
}