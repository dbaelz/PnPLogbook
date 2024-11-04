package de.dbaelz.pnp.logbook

import de.dbaelz.pnp.logbook.di.koinModule
import de.dbaelz.pnp.logbook.features.ApiRoute
import de.dbaelz.pnp.logbook.features.apiBasePath
import de.dbaelz.pnp.logbook.features.experience.Experience
import de.dbaelz.pnp.logbook.features.experience.ExperienceDTO
import de.dbaelz.pnp.logbook.features.experience.ExperienceRepository
import helper.now
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.Test


class RoutesTest : KoinTest {
    private val experienceRepository = mockk<ExperienceRepository>()

    private val koinTestModule = module {
        single<ExperienceRepository> { experienceRepository }
    }

    @Test
    fun testRoot() = testApplication {
        application {
            module()
        }

        val expected = """            
            <!DOCTYPE html>
            <html>
              <head>
                <title>PnP Logbook</title>
              </head>
              <body>
                <h1>Welcome to the PnP Logbook</h1>
                <p>This is the root page of the PnP Logbook. The REST API is available at $apiBasePath
                  <ul>
                    <li><a href="${ApiRoute.LOGBOOK.fullResourcePath}">Logbook</a></li>
                    <li><a href="${ApiRoute.EXPERIENCE.fullResourcePath}">Experience</a></li>
                    <li><a href="${ApiRoute.CURRENCY.fullResourcePath}">Currency</a></li>
                    <li><a href="${ApiRoute.PERSONS.fullResourcePath}">Persons</a></li>
                    <li><a href="${ApiRoute.GROUPS.fullResourcePath}">Groups</a></li>
                    <li><a href="${ApiRoute.PLACES.fullResourcePath}">Places</a></li>
                  </ul>
                </p>
              </body>
            </html>

        """.trimIndent()

        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(expected, response.bodyAsText())
    }

    @Test
    fun testApiExperience_without_items() = testApplication {
        coEvery { experienceRepository.getExperience() } returns emptyList()

        application {
            module(listOf(koinModule, koinTestModule))
        }

        coEvery { experienceRepository.getExperience() } returns emptyList()

        val expected = ExperienceDTO(0, emptyList())

        val response = client.get(ApiRoute.EXPERIENCE.fullResourcePath)
        val actual = Json.decodeFromString(ExperienceDTO.serializer(), response.bodyAsText())

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(expected, actual)
    }

    @Test
    fun testApiExperience_with_items() = testApplication {
        val experience = listOf(
            Experience(0, LocalDateTime.now(), 1, "reason1"),
            Experience(1, LocalDateTime.now(), 2, "reason2")
        )

        coEvery { experienceRepository.getExperience() } returns experience

        application {
            module(listOf(koinModule, koinTestModule))
        }

        val expected = ExperienceDTO(3, experience)

        val response = client.get(ApiRoute.EXPERIENCE.fullResourcePath)
        val actual = Json.decodeFromString(ExperienceDTO.serializer(), response.bodyAsText())

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(expected, actual)
    }
}