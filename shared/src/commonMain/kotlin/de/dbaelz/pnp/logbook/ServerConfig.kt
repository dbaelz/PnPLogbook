package de.dbaelz.pnp.logbook

const val SERVER_PORT = 8080

const val HEADER_X_PLATFORM = "X-Platform"

expect fun getServerHost(): String

// Fixme: For testing. Obviously not secure to hardcode the credentials
const val BASIC_AUTH_USERNAME = "admin"
const val BASIC_AUTH_PASSWORD = "password123"