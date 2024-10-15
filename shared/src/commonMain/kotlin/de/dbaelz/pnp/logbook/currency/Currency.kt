package de.dbaelz.pnp.logbook.currency

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
data class Currency(val coins: Coins, val reason: String)