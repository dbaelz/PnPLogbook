package de.dbaelz.pnp.logbook.helper

import io.github.aakira.napier.Napier
import org.koin.core.logger.Level
import org.koin.core.logger.MESSAGE


class NapierKoinLogger : org.koin.core.logger.Logger() {
    override fun display(level: Level, msg: MESSAGE) {
        when (level) {
            Level.INFO, Level.NONE -> Napier.i(msg)
            Level.DEBUG -> Napier.d(msg)
            Level.WARNING -> Napier.w(msg)
            Level.ERROR -> Napier.e(msg)
        }
    }
}