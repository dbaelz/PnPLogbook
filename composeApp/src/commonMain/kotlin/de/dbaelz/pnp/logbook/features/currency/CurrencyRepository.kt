package de.dbaelz.pnp.logbook.features.currency

import de.dbaelz.pnp.logbook.features.ApiRoute
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class CurrencyRepository(private val httpClient: HttpClient) {
    suspend fun getCurrency(): CurrencyDTO {
        return httpClient.get(ApiRoute.CURRENCY.resource).body()
    }

    suspend fun addCurrency(coins: Coins, reason: String): CurrencyDTO {
        return httpClient.post(ApiRoute.CURRENCY.resource) {
            contentType(ContentType.Application.Json)
            setBody(AddCurrency(coins, reason))
        }.body()
    }
}