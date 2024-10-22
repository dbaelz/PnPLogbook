package de.dbaelz.pnp.logbook

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

fun appInit() {
    Napier.base(DebugAntilog())
}