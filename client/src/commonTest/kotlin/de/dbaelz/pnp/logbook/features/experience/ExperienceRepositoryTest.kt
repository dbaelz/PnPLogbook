package de.dbaelz.pnp.logbook.features.experience

import de.dbaelz.pnp.logbook.features.ApiRoute
import de.dbaelz.pnp.logbook.helper.now
import de.dbaelz.pnp.logbook.helper.serializeToByteReadChannel
import de.dbaelz.pnp.logbook.network.createHttpClient
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class ExperienceRepositoryTest {
    @Test
    fun getExperience_returns_ExperienceDTO() = runTest {
        val expected = ExperienceDTO(
            total = 42, entries = listOf(
                Experience(id = 1, date = LocalDateTime.now(), experience = 40, reason = "reason1"),
                Experience(id = 2, date = LocalDateTime.now(), experience = 2, reason = "reason2")
            )
        )

        val mockEngine = MockEngine { _ ->
            respond(
                content = expected.serializeToByteReadChannel(ExperienceDTO.serializer()),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val experienceRepository = ExperienceRepository(
            httpClient = createHttpClient(mockEngine)
        )

        val actual = experienceRepository.getExperience()

        assertEquals(expected, actual)
    }

    @Test
    fun getExperience_with_internal_server_error_throws_exception() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(
                content = "",
                status = HttpStatusCode.InternalServerError
            )
        }

        val experienceRepository = ExperienceRepository(
            httpClient = createHttpClient(mockEngine)
        )

        assertFailsWith<Exception> {
            experienceRepository.getExperience()
        }
    }

    @Test
    fun addExperience_with_parameters_returns_ExperienceDTO() = runTest {
        val expectedExperience = 42
        val expectedReason = "reason1"
        val expectedResponse = ExperienceDTO(
            total = 42, entries = listOf(
                Experience(id = 1, date = LocalDateTime.now(), experience = 42, reason = "reason1")
            )
        )

        val mockEngine = MockEngine { request ->
            assertEquals(ApiRoute.EXPERIENCE.fullResourcePath, request.url.encodedPath)
            assertEquals(HttpMethod.Post, request.method)
            assertEquals(ContentType.Application.Json, request.body.contentType)

            val requestJson = request.body.toByteArray().decodeToString()
            val addExperience = Json.decodeFromString(AddExperience.serializer(), requestJson)
            assertEquals(expectedExperience, addExperience.experience)
            assertEquals(expectedReason, addExperience.reason)

            respond(
                content = expectedResponse.serializeToByteReadChannel(ExperienceDTO.serializer()),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val experienceRepository = ExperienceRepository(
            httpClient = createHttpClient(mockEngine)
        )

        val actual = experienceRepository.addExperience(
            experience = 42,
            reason = "reason1"
        )

        assertEquals(expectedResponse, actual)
    }

    @Test
    fun addExperience_with_parameters_and_internal_server_error_throws_exception() = runTest {
        val expectedExperience = 42
        val expectedReason = "reason1"

        val mockEngine = MockEngine { request ->
            assertEquals(ApiRoute.EXPERIENCE.fullResourcePath, request.url.encodedPath)
            assertEquals(HttpMethod.Post, request.method)
            assertEquals(ContentType.Application.Json, request.body.contentType)

            val requestJson = request.body.toByteArray().decodeToString()
            val addExperience = Json.decodeFromString(AddExperience.serializer(), requestJson)
            assertEquals(expectedExperience, addExperience.experience)
            assertEquals(expectedReason, addExperience.reason)

            respond(
                content = "",
                status = HttpStatusCode.InternalServerError
            )
        }

        val experienceRepository = ExperienceRepository(
            httpClient = createHttpClient(mockEngine)
        )

        assertFailsWith<Exception> {
            experienceRepository.addExperience(
                experience = 42,
                reason = "reason1"
            )
        }
    }
}