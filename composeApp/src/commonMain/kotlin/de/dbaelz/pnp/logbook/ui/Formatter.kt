package de.dbaelz.pnp.logbook.ui

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.char

val localDateTimeFormat = LocalDateTime.Format {
    dayOfMonth()
    char('.')
    monthNumber()
    char('.')
    year()
}