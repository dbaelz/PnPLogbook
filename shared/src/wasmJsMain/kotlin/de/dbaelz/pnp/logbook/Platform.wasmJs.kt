package de.dbaelz.pnp.logbook

actual fun getPlatform(): Platform = Platform.Wasm

// Fixme: Only for local testing
actual fun getServerHost(): String = "localhost"