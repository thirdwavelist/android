package com.thirdwavelist.coficiando.home.presentation

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.thirdwavelist.coficiando.core.coroutines.TestDispatcherProvider
import com.thirdwavelist.coficiando.core.data.cafes.CafeRepository
import com.thirdwavelist.coficiando.core.domain.cafe.*
import com.thirdwavelist.coficiando.core.livedata.test
import com.thirdwavelist.coficiando.core.logging.BusinessEventLogger
import com.thirdwavelist.coficiando.core.logging.ErrorEventLogger
import com.thirdwavelist.coficiando.home.domain.GetAllCafesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class HomeFragmentViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mockRepository: CafeRepository = mock()
    private val testCoroutineDispatcher = TestDispatcherProvider()
    private val getAllCafesUseCase = GetAllCafesUseCase(mockRepository, testCoroutineDispatcher)
    private val mockErrorEventLogger: ErrorEventLogger = mock()
    private val mockBusinessEventLogger: BusinessEventLogger = mock()

    private lateinit var viewModel: HomeFragmentViewModel

    @Before
    fun setup() {
        viewModel = HomeFragmentViewModel(getAllCafesUseCase, mockErrorEventLogger, mockBusinessEventLogger)
    }

    @Test
    fun `When the viewModel is initialized, Then the viewState is Loading`() {
        // Then
        assertThat(viewModel.viewState.value).isEqualTo(HomeViewState.Loading)
    }

    @Test
    fun `Given error from use case, When #loadCafes() called, Then the viewState is Error`() {
        // Given
        givenUseCaseError()

        // When
        viewModel.loadCafes()

        // Then
        assertThat(viewModel.viewState.value).isEqualTo(HomeViewState.Error)
    }

    @Test
    fun `Given success from use case but empty list returned, When #loadCafes() called, Then the viewState is Error`() {
        // Given
        val listOfCafes = emptyList<CafeItem>()
        givenUseCaseSuccess(listOfCafes)

        // When
        viewModel.loadCafes()

        // Then
        assertThat(viewModel.viewState.value).isEqualTo(HomeViewState.Error)
    }

    @Test
    fun `Given success from use case, When #loadCafes() called, Then the viewState is Success`() {
        // Given
        val listOfCafes = listOf(feketeEntity)
        givenUseCaseSuccess(listOfCafes)

        // When
        viewModel.loadCafes()

        // Then
        assertThat(viewModel.viewState.value).isEqualTo(HomeViewState.Success(listOfCafes))
    }

    @Test
    fun `When #onRetryTapped(), Then ReloadData event is emitted`() {
        // When
        viewModel.onRetryTapped()
        val viewEvents = viewModel.viewEvents.test()

        // Then
        assertThat(viewEvents.first().peekContent()).isEqualTo(HomeEvents.ReloadData)
    }

    @Test
    fun `Given cafe, When #onCafeTapped(Cafe,List), Then NavigateToUrl event is emmitted with given params`() {
        // Given
        val expectedCafe = feketeEntity
        val expectedExtras = emptyList<Pair<View, String>>()

        // When
        viewModel.onCafeTapped(feketeEntity, expectedExtras)
        val viewEvents = viewModel.viewEvents.test()

        // Then
        assertThat(viewEvents.first().peekContent()).isEqualTo(HomeEvents.NavigateToDetails(expectedCafe, expectedExtras))
    }

    private fun givenUseCaseError() = runBlocking {
        given(mockRepository.getAll()).willReturn(null)
    }

    private fun givenUseCaseSuccess(cafes: List<CafeItem> = listOf(feketeEntity)) = runBlocking {
        given(mockRepository.getAll()).willReturn(cafes)
    }

    private val feketeEntity = CafeItem(
            UUID.fromString("4029172e-c30e-40a9-957b-c85252db248a"),
            "Fekete",
            "https://assets.thirdwavelist.com/thumb/4029172e-c30e-40a9-957b-c85252db248a.jpg",
            SocialItem(null, "", " "),
            "ChIJ-UU5E0PcQUcRP0iqvvmBTO8",
            GearInfoItem("La Marzocco", "Victoria Arduino Mythos, Mahlkönig EK43"),
            BeanInfoItem("", "Casino Mocca", true, false, true, false, false),
            BrewInfoItem(true, false, true, true, false, true)
    )
}