package de.dbaelz.pnp.logbook

import android.os.Build

actual fun getPlatform(): Platform = Platform.Android

// Fixme: Only for local testing
actual fun getServerHost(): String = "10.0.2.2"