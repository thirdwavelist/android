package com.thirdwavelist.coficiando.home.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.then
import com.thirdwavelist.coficiando.core.coroutines.TestDispatcherProvider
import com.thirdwavelist.coficiando.core.data.cafes.CafeRepository
import com.thirdwavelist.coficiando.coreutils.usecase.None
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAllCafesUseCaseTest {

    private val mockRepository: CafeRepository = mock()
    private val testDispatcherProvider = TestDispatcherProvider()
    private val useCase: GetAllCafesUseCase by lazy { GetAllCafesUseCase(mockRepository, testDispatcherProvider) }

    @Test
    fun `When execute is called then CafeRepository#getAll() gets called`(): Unit = runBlocking {
        // When
        useCase.withParams(None).execute { }

        // Then
        then(mockRepository).should().getAll()
    }
}