package de.dbaelz.pnp.logbook.features.currency

import de.dbaelz.pnp.logbook.network.httpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class CurrencyRepository {
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