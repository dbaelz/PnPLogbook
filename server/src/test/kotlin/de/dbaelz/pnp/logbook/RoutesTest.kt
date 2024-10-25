package de.dbaelz.pnp.logbook

import de.dbaelz.pnp.logbook.features.ApiRoute
import de.dbaelz.pnp.logbook.features.apiBasePath
import de.dbaelz.pnp.logbook.features.experience.ExperienceDTO
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
    fun testApiExperience() = testApplication {
        application {
            module()
        }

        val expected = ExperienceDTO(0, emptyList())

        val response = client.get(ApiRoute.EXPERIENCE.fullResourcePath)
        val actual = Json.decodeFromString(ExperienceDTO.serializer(), response.bodyAsText())

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(expected, actual)
    }
}