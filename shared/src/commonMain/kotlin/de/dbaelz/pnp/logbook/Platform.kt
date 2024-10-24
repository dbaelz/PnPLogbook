package de.dbaelz.pnp.logbook

enum class Platform(val text: String) {
    Android("Android"),
    IOS("iOS"),
    JVM("Desktop"),
    Wasm("Web"),
    Unknown("Unknown");
}

expect fun getPlatform(): Platform

expect fun getServerHost(): String