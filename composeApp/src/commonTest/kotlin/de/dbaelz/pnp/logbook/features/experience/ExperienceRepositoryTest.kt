package de.dbaelz.pnp.logbook.features.experience

import de.dbaelz.pnp.logbook.helper.now
import de.dbaelz.pnp.logbook.helper.serializeToByteReadChannel
import de.dbaelz.pnp.logbook.network.createHttpClient
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals


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
}