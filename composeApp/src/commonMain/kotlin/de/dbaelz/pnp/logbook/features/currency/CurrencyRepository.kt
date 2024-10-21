package de.dbaelz.pnp.logbook.features.currency

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class CurrencyRepository(private val httpClient: HttpClient) {
    suspend fun getCurrency(): CurrencyDTO {
        return httpClient.get("currency").body()
    }

    suspend fun addCurrency(coins: Coins, reason: String): CurrencyDTO {
        return httpClient.post("currency") {
            contentType(ContentType.Application.Json)
            setBody(AddCurrency(coins, reason))
        }.body()
    }
}