package de.dbaelz.pnp.logbook.features.experience

import de.dbaelz.pnp.logbook.helper.executeQuery
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

interface ExperienceRepository {
    suspend fun getExperience(): List<Experience>

    suspend fun add(experience: AddExperience)
}

class ExperienceRepositoryImpl : ExperienceRepository {
    private object ExperienceTable : IntIdTable() {
        val date = datetime("date").defaultExpression(CurrentDateTime)
        val experience = integer("experience")
        val reason = varchar("reason", 255)
    }

    init {
        transaction {
            SchemaUtils.create(ExperienceTable)
        }
    }

    override suspend fun getExperience(): List<Experience> {
        return executeQuery {
            ExperienceTable.selectAll()
                .map {
                    Experience(
                        id = it[ExperienceTable.id].value,
                        date = it[ExperienceTable.date],
                        experience = it[ExperienceTable.experience],
                        reason = it[ExperienceTable.reason]
                    )
                }
        }
    }

    override suspend fun add(experience: AddExperience) {
        executeQuery {
            ExperienceTable.insert {
                it[ExperienceTable.experience] = experience.experience
                it[reason] = experience.reason
            }
        }
    }
}