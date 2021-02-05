package com.thirdwavelist.coficiando.core.data.cafes

import com.haroldadmin.cnradapter.NetworkResponse
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.thirdwavelist.coficiando.core.data.cafes.mapper.CafeItemDtoToCafeEntityMapper
import com.thirdwavelist.coficiando.core.data.db.CafeDao
import com.thirdwavelist.coficiando.core.data.network.ThirdWaveListService
import com.thirdwavelist.coficiando.core.data.network.model.CafeItemDto
import com.thirdwavelist.coficiando.core.domain.cafe.*
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.util.UUID

class CafeRepositoryTest {

    private val mockService: ThirdWaveListService = mock()
    private val mapper: CafeItemDtoToCafeEntityMapper = CafeItemDtoToCafeEntityMapper()
    private val mockDao: CafeDao = mock()

    private lateinit var repository: CafeRepository

    @Before
    fun setup() {
        repository = CafeRepository(mockDao, mockService, mapper)
    }

    @Test
    fun `Given valid remote response and local data When getAll is called Then returns remote mapped entities`(): Unit = runBlocking {
        // Given
        givenDaoReturns(listOf(Entity1))
        givenServiceReturns(listOf(DataObject1, DataObject2))

        // When
        val result = repository.getAll()

        // Then
        verify(mockDao).getAll()
        verify(mockService).getCafes()
        assertThat(result).satisfies {
            assertThat(it).hasSize(2)
            assertThat(it).usingRecursiveComparison().isEqualTo(listOf(Entity1, Entity2))
        }
    }

    @Test
    fun `Given valid remote response When getAll is called Then remote data is inserted into local`(): Unit = runBlocking {
        // Given
        givenServiceReturns(listOf(DataObject1, DataObject2))

        // When
        repository.getAll()

        // Then
        verify(mockDao).insertAll(listOf(Entity1, Entity2))
    }

    @Test
    fun `Given remote response fails but local data When getAll is called Then local data is returned`(): Unit = runBlocking {
        // Given
        givenDaoReturns(listOf(Entity1))
        givenServiceFails()

        // When
        val result = repository.getAll()

        // Then
        verify(mockDao).getAll()
        assertThat(result).usingRecursiveComparison().isEqualTo(listOf(Entity1))
    }

    private fun givenServiceFails() {
        given { runBlocking { mockService.getCafes() } }.willReturn(NetworkResponse.NetworkError(IOException("Test")))
    }

    private fun givenDaoReturns(listOf: List<CafeItem>) {
        given { runBlocking { mockDao.getAll() } }.willReturn(listOf)
    }

    private fun givenServiceReturns(listOf: List<CafeItemDto>) {
        given { runBlocking { mockService.getCafes() } }.willReturn(NetworkResponse.Success(listOf, null, 200))
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