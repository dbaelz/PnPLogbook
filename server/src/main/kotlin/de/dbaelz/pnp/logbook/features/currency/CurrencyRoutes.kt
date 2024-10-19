package de.dbaelz.pnp.logbook.features.currency

import de.dbaelz.pnp.logbook.features.currency.AddCurrency
import de.dbaelz.pnp.logbook.features.currency.Coins
import de.dbaelz.pnp.logbook.features.currency.Currency
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Route.registerCurrencyRoutes() {
    val currencyRepository = CurrencyRepository()

    route("/currency") {
        get {
            val currency = currencyRepository.getAmountAndEntries()

            call.respond(
                CurrencyResponse(
                    total = currency.first,
                    entries = currency.second
                )
            )
        }

        post {
            try {
                val currency = call.receive<AddCurrency>()
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

@Serializable
data class CurrencyResponse(val total: Coins, val entries: List<Currency>)