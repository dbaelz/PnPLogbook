package de.dbaelz.pnp.logbook.features.actionlog

import de.dbaelz.pnp.logbook.Platform
import kotlinx.serialization.Serializable

@Serializable
data class ActionLogItem(
    val platform: Platform,
    val httpMethod: String,
    val path: String
)