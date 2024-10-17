package de.dbaelz.pnp.logbook.xp

import de.dbaelz.pnp.logbook.helper.executeQuery
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ExperienceRepository {
    private object ExperienceTable : IntIdTable() {
        val experience = integer("experience")
        val reason = varchar("reason", 255)
    }

    init {
        transaction {
            SchemaUtils.create(ExperienceTable)
        }
    }

    suspend fun getExperience(): List<Experience> {
        return executeQuery {
            ExperienceTable.selectAll()
                .map {
                    Experience(
                        id = it[ExperienceTable.id].value,
                        experience = it[ExperienceTable.experience],
                        reason = it[ExperienceTable.reason]
                    )
                }
        }
    }

    suspend fun add(experience: Int, reason: String) {
        executeQuery {
            ExperienceTable.insert {
                it[this.experience] = experience
                it[this.reason] = reason
            }
        }
    }
}