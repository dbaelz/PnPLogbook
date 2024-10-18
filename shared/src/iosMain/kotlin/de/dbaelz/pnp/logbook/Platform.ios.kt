package de.dbaelz.pnp.logbook

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

// Fixme: Only for local testing
actual fun getServerHost(): String = "127.0.0.1"