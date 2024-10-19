package de.dbaelz.pnp.logbook.features.subject

import kotlinx.serialization.Serializable

@Serializable
data class Subject(val id: Int, val name: String, val description: String, val notes: String)

@Serializable
data class AddSubject(val name: String, val description: String, val notes: String)