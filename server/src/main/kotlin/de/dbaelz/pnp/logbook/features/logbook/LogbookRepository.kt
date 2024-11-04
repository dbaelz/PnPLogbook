package de.dbaelz.pnp.logbook.features.logbook

import de.dbaelz.pnp.logbook.helper.executeQuery
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface LogbookRepository {
    suspend fun getLogbook(): List<LogbookEntry>

    suspend fun getLogbookEntry(id: Int): LogbookEntry?

    suspend fun add(logbook: AddLogbookEntry)

    suspend fun delete(id: Int)
}

class LogbookRepositoryImpl : LogbookRepository {
    private object LogbookTable : IntIdTable() {
        val date = datetime("date").defaultExpression(CurrentDateTime)
        val location = varchar("location", 255)
        val text = text("text")
    }

    init {
        transaction {
            SchemaUtils.create(LogbookTable)
        }
    }

    override suspend fun getLogbook(): List<LogbookEntry> {
        return executeQuery {
            LogbookTable.selectAll()
                .map {
                    LogbookEntry(
                        id = it[LogbookTable.id].value,
                        date = it[LogbookTable.date],
                        location = it[LogbookTable.location],
                        text = it[LogbookTable.text]
                    )
                }
        }
    }

    override suspend fun getLogbookEntry(id: Int): LogbookEntry? {
        return executeQuery {
            LogbookTable.selectAll()
                .where { LogbookTable.id eq id }
                .map {
                    LogbookEntry(
                        id = it[LogbookTable.id].value,
                        date = it[LogbookTable.date],
                        location = it[LogbookTable.location],
                        text = it[LogbookTable.text]
                    )
                }
                .firstOrNull()
        }
    }

    override suspend fun add(logbook: AddLogbookEntry) {
        executeQuery {
            LogbookTable.insert {
                it[location] = logbook.location
                it[text] = logbook.text
            }
        }
    }

    override suspend fun delete(id: Int) {
        executeQuery {
            LogbookTable.deleteWhere { LogbookTable.id eq id }
        }
    }
}