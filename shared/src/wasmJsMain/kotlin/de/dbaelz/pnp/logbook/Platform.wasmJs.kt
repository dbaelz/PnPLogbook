package de.dbaelz.pnp.logbook

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

// Fixme: Only for local testing
actual fun getServerHost(): String = "127.0.0.1"