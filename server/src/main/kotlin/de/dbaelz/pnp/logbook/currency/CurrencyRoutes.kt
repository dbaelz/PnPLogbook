package de.dbaelz.pnp.logbook.currency

import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.registerCurrencyRoutes() {
    val currencyRepository = CurrencyRepository()

    routing {
        route("/currency") {
            get {
                val currency = currencyRepository.getAmountAndItems()

                call.respond(
                    CurrencyResponse(
                        total = currency.first,
                        entries = currency.second
                    )
                )
            }

            post {
                try {
                    val currency = call.receive<Currency>()
                    currencyRepository.add(currency)
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
data class CurrencyResponse(val total: Coins, val entries: List<Currency>)