package de.dbaelz.pnp.logbook

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun getServerHost(): String = "127.0.0.1"