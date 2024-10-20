package de.dbaelz.pnp.logbook.features.currency

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Coins(
    val copper: Int = 0,
    val silver: Int = 0,
    val electrum: Int = 0,
    val gold: Int = 0,
    val platinum: Int = 0
)

@Serializable
data class Currency(val id: Int, val date: LocalDateTime, val coins: Coins, val reason: String)

@Serializable
data class CurrencyDTO(val total: Coins, val entries: List<Currency>)

@Serializable
data class AddCurrency(val coins: Coins, val reason: String)