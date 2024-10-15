package de.dbaelz.pnp.logbook.xp

import kotlinx.serialization.Serializable

@Serializable
data class Experience(val experience: Int, val reason: String)