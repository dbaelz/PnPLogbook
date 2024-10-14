package de.dbaelz.pnp.logbook

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform