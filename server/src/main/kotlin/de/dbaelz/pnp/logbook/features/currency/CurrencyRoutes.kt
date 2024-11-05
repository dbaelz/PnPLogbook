package de.dbaelz.pnp.logbook.features.currency

import de.dbaelz.pnp.logbook.features.ApiRoute
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerCurrencyRoutes(currencyRepository: CurrencyRepository) {
    route(ApiRoute.CURRENCY.resourcePath) {
        get {
            val currency = currencyRepository.getAmountAndEntries()

            call.respond(
                CurrencyDTO(
                    total = currency.first,
                    entries = currency.second
                )
            )
        }

        post {
            try {
                val addCurrency = call.receive<AddCurrency>()
                currencyRepository.add(addCurrency)

                val currency = currencyRepository.getAmountAndEntries()
                call.respond(
                    CurrencyDTO(
                        total = currency.first,
                        entries = currency.second
                    )
                )
            } catch (exception: CannotTransformContentToTypeException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (exception: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (exception: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}