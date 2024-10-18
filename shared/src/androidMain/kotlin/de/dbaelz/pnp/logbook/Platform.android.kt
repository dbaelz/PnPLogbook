package de.dbaelz.pnp.logbook

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

// TODO: Only for testing on emulator
actual fun getServerHost(): String = "10.0.2.2"