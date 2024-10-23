package de.dbaelz.pnp.logbook

enum class Platform {
    Android,
    IOS,
    JVM,
    Wasm,
    Unknown
}

expect fun getPlatform(): Platform

expect fun getServerHost(): String