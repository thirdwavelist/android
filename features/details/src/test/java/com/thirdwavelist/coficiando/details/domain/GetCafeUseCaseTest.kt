package com.thirdwavelist.coficiando.details.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.then
import com.thirdwavelist.coficiando.core.coroutines.TestDispatcherProvider
import com.thirdwavelist.coficiando.core.data.cafes.CafeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.UUID

@ExperimentalCoroutinesApi
class GetCafeUseCaseTest {

    private val mockRepository: CafeRepository = mock()
    private val testDispatcherProvider = TestDispatcherProvider()
    private val useCase: GetCafeUseCase by lazy { GetCafeUseCase(mockRepository, testDispatcherProvider) }

    @Test
    fun `When execute is called then CafeRepository#getCafe(UUID) gets called`(): Unit = runBlocking {
        // Given
        val expectedUUID = UUID.randomUUID()

        // When
        useCase.withParams(expectedUUID).execute { }

        // Then
        then(mockRepository).should().getById(expectedUUID)
    }
}