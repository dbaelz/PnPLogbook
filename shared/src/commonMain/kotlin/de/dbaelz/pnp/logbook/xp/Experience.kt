package de.dbaelz.pnp.logbook.xp

import kotlinx.serialization.Serializable

@Serializable
data class Experience(val id: Int, val experience: Int, val reason: String)

@Serializable
data class AddExperience(val experience: Int, val reason: String)