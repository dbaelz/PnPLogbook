package de.dbaelz.pnp.logbook

import de.dbaelz.pnp.logbook.features.experience.AddExperience
import de.dbaelz.pnp.logbook.features.experience.ExperienceDTO
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import junit.framework.TestCase.assertEquals
import kotlinx.serialization.json.Json
import kotlin.test.Test


class RoutesTest {
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
                <p>This is the root page of the PnP Logbook. The REST API is available at /api
                  <ul>
                    <li><a href="/api/logbook">Logbook</a></li>
                    <li><a href="/api/experience">Experience</a></li>
                    <li><a href="/api/currency">Currency</a></li>
                    <li><a href="/api/persons">Persons</a></li>
                    <li><a href="/api/groups">Groups</a></li>
                    <li><a href="/api/places">Places</a></li>
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
    fun testApiExperience() = testApplication {
        application {
            module()
        }

        val expected = ExperienceDTO(0, emptyList())

        val response = client.get("/api/experience")
        val actual = Json.decodeFromString(ExperienceDTO.serializer(), response.bodyAsText())

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(expected, actual)
    }
}