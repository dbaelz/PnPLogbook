package de.dbaelz.pnp.logbook

import de.dbaelz.pnp.logbook.features.ApiRoute
import de.dbaelz.pnp.logbook.features.actionlog.ActionLog
import de.dbaelz.pnp.logbook.features.actionlog.ActionLogRepository
import de.dbaelz.pnp.logbook.features.actionlog.registerActionLogRoutes
import de.dbaelz.pnp.logbook.features.apiBasePath
import de.dbaelz.pnp.logbook.features.currency.CurrencyRepository
import de.dbaelz.pnp.logbook.features.currency.registerCurrencyRoutes
import de.dbaelz.pnp.logbook.features.experience.ExperienceRepository
import de.dbaelz.pnp.logbook.features.experience.registerExperienceRoutes
import de.dbaelz.pnp.logbook.features.logbook.LogbookRepository
import de.dbaelz.pnp.logbook.features.logbook.registerLogbookRoutes
import de.dbaelz.pnp.logbook.features.rootHtml
import de.dbaelz.pnp.logbook.features.subject.SubjectRepository
import de.dbaelz.pnp.logbook.features.subject.registerSubjectRoutes
import de.dbaelz.pnp.logbook.helper.NapierKoinLogger
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import io.ktor.server.sse.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.koin.core.module.Module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import java.io.File
import java.sql.Connection

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = getServerHost(), module = Application::module)
        .start(wait = true)
}

fun Application.module(
    // Workaround to use different injections for tests
    // We can't use loadKoinModules in tests because the properties are injected in this
    // extension function and therefore loadKoinModules doesn't change that properties afterward
    // Might change when https://github.com/InsertKoinIO/koin/issues/1716 is solved
    koinModules: List<Module> = listOf(de.dbaelz.pnp.logbook.di.koinModule)
) {
    Napier.base(DebugAntilog())

    install(Koin) {
        logger(NapierKoinLogger())
        modules(koinModules)
    }

    configureDatabase()

    install(CORS) {
        // Fixme: Only for local testing
        anyHost()
        anyMethod()
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HEADER_X_PLATFORM)

        allowSameOrigin = true
        allowCredentials = true

        // TODO: Fix CORS and make Wasm working with SSE...
    }

    install(Authentication) {
        basic("auth") {
            realm = BASIC_AUTH_REALM
            validate { (username, password) ->
                if (username == BASIC_AUTH_USERNAME && password == BASIC_AUTH_PASSWORD) {
                    UserIdPrincipal(username)
                } else {
                    null
                }
            }
        }
    }

    install(ContentNegotiation) {
        json()
    }

    val actionLogRepository by inject<ActionLogRepository>()

    // Custom plugin that logs API Calls as action log and emits them via ActionLogRepository
    install(ActionLog) {
        repository = actionLogRepository
    }

    // Use ShutDownUrl class without the plugin to trigger it manually
    val shutdown = ShutDownUrl("") { 0 }

    install(SSE)

    val experienceRepository by inject<ExperienceRepository>()
    val currencyRepository by inject<CurrencyRepository>()
    val logbookRepository by inject<LogbookRepository>()
    val subjectRepository by inject<SubjectRepository>()

    routing {
        get(rootPath) {
            call.respondHtml(
                status = HttpStatusCode.OK, block = rootHtml()
            )
        }

        route(apiBasePath) {
            authenticate("auth") {
                get(ApiRoute.SHUTDOWN.resourcePath) {
                    // Calling the shutdown logic provided by the shutdown url plugin
                    shutdown.doShutdown(call)
                }
            }

            registerExperienceRoutes(experienceRepository)
            registerCurrencyRoutes(currencyRepository)
            registerLogbookRoutes(logbookRepository)
            registerSubjectRoutes(subjectRepository)

            registerActionLogRoutes(actionLogRepository)
        }
    }
}

private fun configureDatabase() {
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    File("./data").mkdir()
    Database.connect("jdbc:sqlite:data/data.db", "org.sqlite.JDBC")
}