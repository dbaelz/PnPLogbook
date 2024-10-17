package de.dbaelz.pnp.logbook.helper

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> executeQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }