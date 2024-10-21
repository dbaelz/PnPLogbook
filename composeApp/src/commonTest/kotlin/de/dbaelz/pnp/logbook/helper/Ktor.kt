package de.dbaelz.pnp.logbook.helper

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

fun <T> T.serializeToByteReadChannel(serializer: KSerializer<T>): ByteReadChannel {
    val json = Json { prettyPrint = false }
    val serializedString = json.encodeToString(serializer, this)
    return ByteReadChannel(serializedString.toByteArray())
}