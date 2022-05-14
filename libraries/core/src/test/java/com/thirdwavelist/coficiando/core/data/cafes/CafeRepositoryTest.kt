package com.thirdwavelist.coficiando.core.data.cafes

import com.haroldadmin.cnradapter.NetworkResponse
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.then
import com.thirdwavelist.coficiando.core.data.cafes.mapper.CafeItemDtoToCafeEntityMapper
import com.thirdwavelist.coficiando.core.data.db.CafeDao
import com.thirdwavelist.coficiando.core.data.network.ThirdWaveListService
import com.thirdwavelist.coficiando.core.data.network.model.CafeItemDto
import com.thirdwavelist.coficiando.core.data.network.model.ErrorResponseDto
import com.thirdwavelist.coficiando.core.domain.cafe.*
import com.thirdwavelist.coficiando.core.logging.BusinessEventLogger
import com.thirdwavelist.coficiando.core.logging.ErrorEventLogger
import com.thirdwavelist.coficiando.coreutils.logging.BusinessEvent
import com.thirdwavelist.coficiando.coreutils.logging.ErrorEvent
import com.thirdwavelist.coficiando.coreutils.logging.ErrorPriority
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.util.UUID
import java.util.function.Consumer

class CafeRepositoryTest {

    private val mockService: ThirdWaveListService = mock()
    private val mapper: CafeItemDtoToCafeEntityMapper = CafeItemDtoToCafeEntityMapper()
    private val mockDao: CafeDao = mock()
    private val mockBusinessEventLogger: BusinessEventLogger = mock()
    private val mockErrorEventLogger: ErrorEventLogger = mock()

    private lateinit var repository: CafeRepository

    @Before
    fun setup() {
        repository = CafeRepository(mockDao, mockService, mapper, mockBusinessEventLogger, mockErrorEventLogger)
    }

    @Test
    fun `Given valid remote response and local data When #getAll() is called Then returns remote mapped entities`(): Unit = runBlocking {
        // Given
        givenLocalReturns(listOf(Entity1))
        givenRemoteReturns(listOf(DataObject1, DataObject2))

        // When
        val result = repository.getAll()

        // Then
        then(mockDao).should().getAll()
        then(mockService).should().getCafes()
        then(mockErrorEventLogger).shouldHaveNoInteractions()
        assertThat(result).satisfies(Consumer {
            assertThat(it).hasSize(2)
            assertThat(it).usingRecursiveComparison().isEqualTo(listOf(Entity1, Entity2))
        })
    }

    @Test
    fun `Given valid remote response When #getAll() is called Then business event is logged with expected context`(): Unit = runBlocking {
        // Given
        givenLocalReturns(listOf(Entity1))
        givenRemoteReturns(listOf(DataObject1, DataObject2))

        // When
        repository.getAll()

        // Then
        then(mockBusinessEventLogger).should().log(BusinessEvent("CafeRepository_getAll_remote_success",
                mapOf(
                        "message" to "Successfully fetched latest cafes via #getAll()",
                        "cafeCount" to 2)
        ))
        then(mockErrorEventLogger).shouldHaveNoInteractions()
    }

    @Test
    fun `Given valid remote response When #getAll() is called Then remote data is inserted into local`(): Unit = runBlocking {
        // Given
        givenRemoteReturns(listOf(DataObject1, DataObject2))

        // When
        repository.getAll()

        // Then
        then(mockDao).should().insertAll(listOf(Entity1, Entity2))
        then(mockErrorEventLogger).shouldHaveNoInteractions()
    }

    @Test
    fun `Given remote response fails with network error but local data When #getAll() is called Then local data is returned`(): Unit = runBlocking {
        // Given
        givenLocalReturns(listOf(Entity1))
        val remoteError = givenRemoteNetworkError()

        // When
        val result = repository.getAll()

        // Then
        then(mockDao).should().getAll()
        then(mockErrorEventLogger).should().log(ErrorEvent(ErrorPriority.HIGH, "CafeRepository", remoteError))
        assertThat(result).usingRecursiveComparison().isEqualTo(listOf(Entity1))
    }

    @Test
    fun `Given remote response fails with server error but local data When #getAll() is called Then local data is returned`(): Unit = runBlocking {
        // Given
        givenLocalReturns(listOf(Entity1))
        givenRemoteServerError()

        // When
        val result = repository.getAll()

        // Then
        then(mockDao).should().getAll()
        assertThat(result).usingRecursiveComparison().isEqualTo(listOf(Entity1))
    }

    @Test
    fun `Given remote response fails with unknown error but local data When #getAll() is called Then local data is returned`(): Unit = runBlocking {
        // Given
        givenLocalReturns(listOf(Entity1))
        val remoteError = givenRemoteUnknownError()

        // When
        val result = repository.getAll()

        // Then
        then(mockDao).should().getAll()
        then(mockErrorEventLogger).should().log(ErrorEvent(ErrorPriority.HIGH, "CafeRepository", remoteError))
        assertThat(result).usingRecursiveComparison().isEqualTo(listOf(Entity1))
    }

    @Test
    fun `Given valid remote response and local data When #getById(UUID) is called Then returns remote mapped entities`(): Unit = runBlocking {
        // Given
        val uuid = DataObject2.id
        givenLocalReturns(uuid, Entity2)
        givenRemoteReturns(uuid, DataObject2)

        // When
        val result = repository.getById(uuid)

        // Then
        then(mockDao).should().get(uuid)
        then(mockService).should().getCafe(uuid)
        assertThat(result).usingRecursiveComparison().isEqualTo(Entity2)
    }

    @Test
    fun `Given valid remote response When #getById(UUID) is called Then remote data is inserted into local`(): Unit = runBlocking {
        // Given
        val uuid = DataObject1.id
        givenRemoteReturns(uuid, DataObject1)

        // When
        repository.getById(uuid)

        // Then
        then(mockDao).should().insert(Entity1)
    }

    @Test
    fun `Given remote response fails with network error but local data When #getById(UUID) is called Then local data is returned`(): Unit = runBlocking {
        // Given
        val uuid = Entity1.id
        givenLocalReturns(uuid, Entity1)
        val remoteError = givenRemoteNetworkError()

        // When
        val result = repository.getById(uuid)

        // Then
        then(mockDao).should().get(uuid)
        then(mockErrorEventLogger).should().log(ErrorEvent(ErrorPriority.HIGH, "CafeRepository", remoteError))
        assertThat(result).usingRecursiveComparison().isEqualTo(Entity1)
    }

    @Test
    fun `Given remote response fails with server error but local data When #getById(UUID) is called Then local data is returned`(): Unit = runBlocking {
        // Given
        val uuid = Entity2.id
        givenLocalReturns(uuid, Entity2)
        givenRemoteServerError()

        // When
        val result = repository.getById(uuid)

        // Then
        then(mockDao).should().get(uuid)
        assertThat(result).usingRecursiveComparison().isEqualTo(Entity2)
    }

    @Test
    fun `Given remote response fails with unknown error but local data When #getById(UUID) is called Then local data is returned`(): Unit = runBlocking {
        // Given
        val uuid = Entity1.id
        givenLocalReturns(uuid, Entity1)
        val remoteError = givenRemoteUnknownError()

        // When
        val result = repository.getById(uuid)

        // Then
        then(mockDao).should().get(uuid)
        then(mockErrorEventLogger).should().log(ErrorEvent(ErrorPriority.HIGH, "CafeRepository", remoteError))
        assertThat(result).usingRecursiveComparison().isEqualTo(Entity1)
    }

    private fun givenRemoteNetworkError(): Throwable {
        val givenException = IOException()
        given { runBlocking { mockService.getCafes() } }.willReturn(NetworkResponse.NetworkError(givenException))
        given { runBlocking { mockService.getCafe(any()) } }.willReturn(NetworkResponse.NetworkError(givenException))
        return givenException
    }

    private fun givenRemoteServerError(errorResponseDto: ErrorResponseDto = ErrorResponseDto("Server Error"), errorResponse: Response<ErrorResponseDto> = Response.error(500, "".toResponseBody())): ErrorResponseDto {
        given { runBlocking { mockService.getCafes() } }.willReturn(NetworkResponse.ServerError(errorResponseDto, errorResponse))
        given { runBlocking { mockService.getCafe(any()) } }.willReturn(NetworkResponse.ServerError(errorResponseDto, errorResponse))
        return errorResponseDto
    }

    private fun givenRemoteUnknownError(errorResponse: Response<ErrorResponseDto> = Response.error(500, "".toResponseBody())): Throwable {
        val givenException = RuntimeException()
        given { runBlocking { mockService.getCafes() } }.willReturn(NetworkResponse.UnknownError(givenException, errorResponse))
        given { runBlocking { mockService.getCafe(any()) } }.willReturn(NetworkResponse.UnknownError(givenException, errorResponse))
        return givenException
    }

    private fun givenLocalReturns(listOf: List<CafeItem>) {
        given { runBlocking { mockDao.getAll() } }.willReturn(listOf)
    }

    private fun givenLocalReturns(forCafeId: UUID, cafe: CafeItem) {
        given { runBlocking { mockDao.get(forCafeId) } }.willReturn(cafe)
    }

    private fun givenRemoteReturns(listOf: List<CafeItemDto>) {
        given { runBlocking { mockService.getCafes() } }.willReturn(NetworkResponse.Success(listOf, Response.success<CafeItemDto>(200, null)))
    }

    private fun givenRemoteReturns(forCafeId: UUID, cafe: CafeItemDto) {
        given { runBlocking { mockService.getCafe(forCafeId) } }.willReturn(NetworkResponse.Success(cafe, Response.success<CafeItemDto>(200, null)))
    }

    private companion object {
        private val DataObject1 = generateDataObject("38031424-7bca-419d-a17e-cfc87e032ed8")
        private val DataObject2 = generateDataObject("4cbabfab-4190-460a-bd59-f45ca73429af")
        private val Entity1 = generateEntity("38031424-7bca-419d-a17e-cfc87e032ed8")
        private val Entity2 = generateEntity("4cbabfab-4190-460a-bd59-f45ca73429af")

        private fun generateEntity(uuidString: String): CafeItem {
            return CafeItem(
                    UUID.fromString(uuidString), "cafeName", "https://thumbnailUrl",
                    SocialItem("https://facebookPageUrl", "https://instagramUrl", "https://websiteUrl"),
                    "googlePlaceId",
                    GearInfoItem("Rancilio", "Baratza"),
                    BeanInfoItem("Ethiopia", "Ozone", true, false, true, true, false),
                    BrewInfoItem(true, false, true, true, false, true)
            )
        }

        private fun generateDataObject(uuidString: String): CafeItemDto {
            return CafeItemDto(
                    UUID.fromString(uuidString), "cafeName", "cafeAddress",
                    "https://thumbnailUrl", "https://websiteUrl", "https://facebookPageUrl",
                    "https://instagramUrl", true, true, true,
                    false, false, true, "Rancilio", "Baratza",
                    "V60", "French Press", "Ozone", "Ethiopia", true, false,
                    true, true, false, "3.44", "googlePlaceId"
            )
        }
    }
}

