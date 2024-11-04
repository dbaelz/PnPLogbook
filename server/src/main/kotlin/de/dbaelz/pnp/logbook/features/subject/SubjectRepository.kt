package de.dbaelz.pnp.logbook.features.subject

import de.dbaelz.pnp.logbook.features.subject.SubjectRepository.SubjectType
import de.dbaelz.pnp.logbook.helper.executeQuery
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

interface SubjectRepository {
    enum class SubjectType {
        Person,
        Group,
        Place
    }

    suspend fun getByType(subjectType: SubjectType): List<Subject>

    suspend fun add(subject: AddSubject, subjectType: SubjectType)

    suspend fun update(id: Int, subject: AddSubject)
}

class SubjectRepositoryImpl : SubjectRepository {
    private object SubjectTable : IntIdTable() {
        val name = varchar("name", 255)
        val description = text("description")
        val notes = text("notes")
        val type = enumeration("type", SubjectType::class)
    }

    init {
        transaction {
            SchemaUtils.create(SubjectTable)
        }
    }

    override suspend fun getByType(subjectType: SubjectType): List<Subject> {
        return executeQuery {
            SubjectTable.selectAll()
                .where { SubjectTable.type eq subjectType }
                .map {
                    Subject(
                        id = it[SubjectTable.id].value,
                        name = it[SubjectTable.name],
                        description = it[SubjectTable.description],
                        notes = it[SubjectTable.notes],
                    )
                }
        }
    }

    override suspend fun add(subject: AddSubject, subjectType: SubjectType) {
        executeQuery {
            SubjectTable.insert {
                it[name] = subject.name
                it[description] = subject.description
                it[notes] = subject.notes
                it[type] = subjectType
            }
        }
    }

    override suspend fun update(id: Int, subject: AddSubject) {
        executeQuery {
            SubjectTable.update(
                    where = { SubjectTable.id eq id },
                    body = {
                        it[name] = subject.name
                        it[description] = subject.description
                        it[notes] = subject.notes
                    }
                )
        }
    }
}