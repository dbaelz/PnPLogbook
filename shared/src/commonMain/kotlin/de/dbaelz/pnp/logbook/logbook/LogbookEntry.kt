package de.dbaelz.pnp.logbook.logbook

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class LogbookEntry(
    val id: Int,
    val date: LocalDateTime,
    val location: String,
    val text: String
)

@Serializable
data class AddLogbookEntry(
    val location: String,
    val text: String
)