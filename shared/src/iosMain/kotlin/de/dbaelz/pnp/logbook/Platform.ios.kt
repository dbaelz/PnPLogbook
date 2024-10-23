package de.dbaelz.pnp.logbook


actual fun getPlatform(): Platform = Platform.IOS

// Fixme: Only for local testing
actual fun getServerHost(): String = "127.0.0.1"