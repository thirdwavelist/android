package com.thirdwavelist.coficiando.details.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.thirdwavelist.coficiando.core.coroutines.TestDispatcherProvider
import com.thirdwavelist.coficiando.core.data.cafes.CafeRepository
import com.thirdwavelist.coficiando.core.domain.cafe.*
import com.thirdwavelist.coficiando.core.livedata.test
import com.thirdwavelist.coficiando.details.domain.GetCafeUseCase
import com.thirdwavelist.coficiando.details.presentation.mapper.MapEntityToDetailsViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID


@OptIn(ExperimentalCoroutinesApi::class)
class DetailsFragmentViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mockRepository: CafeRepository = mock()
    private val testCoroutineDispatcher = TestDispatcherProvider()
    private val getCafeUseCase: GetCafeUseCase = GetCafeUseCase(mockRepository, testCoroutineDispatcher)
    private val googlePlaceLink: String = "https://www.google.com/maps/search/?query_place_id="
    private val viewStateMapper = MapEntityToDetailsViewState(googlePlaceLink)
    private lateinit var viewModel: DetailsFragmentViewModel

    @Before
    fun setup() {
        viewModel = DetailsFragmentViewModel(getCafeUseCase, viewStateMapper)
    }

    @Test
    fun `When the viewModel is initialized, Then the viewState is Loading`() {
        // Then
        assertThat(viewModel.viewState.value).isEqualTo(DetailsViewState.Loading)
    }

    @Test
    fun `Given error from use case, When #loadCafes(UUID) called, Then the viewState is Error`() {
        // Given
        val cafeId = UUID.randomUUID()
        givenUseCaseError(cafeId)

        // When
        viewModel.loadCafe(cafeId)

        // Then
        assertThat(viewModel.viewState.value).isEqualTo(DetailsViewState.Error)
    }

    @Test
    fun `Given success from use case but no cafe data, When #loadCafes(UUID) called, Then the viewState is Error`() {
        // Given
        val cafeId = UUID.randomUUID()
        givenUseCaseSuccess(cafeId, null)

        // When
        viewModel.loadCafe(cafeId)

        // Then
        assertThat(viewModel.viewState.value).isEqualTo(DetailsViewState.Error)
    }

    @Test
    fun `Given success from use case, When #loadCafes(UUID) called, Then the viewState is Success`() {
        // Given
        val cafeId = UUID.randomUUID()
        val cafe = feketeEntity
        givenUseCaseSuccess(cafeId, cafe)

        // When
        viewModel.loadCafe(cafeId)

        // Then
        assertThat(viewModel.viewState.value).isEqualTo(feketeViewState)
    }

    @Test
    fun `When #onRetryTapped(), Then ReloadData event is emitted`() {
        // When
        viewModel.onRetryTapped()
        val viewEvents = viewModel.viewEvents.test()

        // Then
        assertThat(viewEvents.first().peekContent()).isEqualTo(DetailsEvents.ReloadData)
    }

    @Test
    fun `Given url, When #onRetryTapped(), Then NavigateToUrl event is emitted with given url`() {
        // Given
        val expectedUrl = "test"

        // When
        viewModel.onLinkButtonTapped(expectedUrl)
        val viewEvents = viewModel.viewEvents.test()

        // Then
        assertThat(viewEvents.first().peekContent()).isEqualTo(DetailsEvents.NavigateToUrl(expectedUrl))
    }

    private fun givenUseCaseSuccess(forCafeId: UUID = any(), cafe: CafeItem?) = runBlocking {
        given(mockRepository.getById(forCafeId)).willReturn(cafe)
    }

    private fun givenUseCaseError(forCafeId: UUID = any()) = runBlocking {
        given(mockRepository.getById(forCafeId)).willReturn(null)
    }

    private val feketeViewState = DetailsViewState.Success(
            "Fekete",
            "https://assets.thirdwavelist.com/thumb/4029172e-c30e-40a9-957b-c85252db248a.jpg",
            "${googlePlaceLink}ChIJ-UU5E0PcQUcRP0iqvvmBTO8",
            "La Marzocco",
            "Victoria Arduino Mythos, Mahlkönig EK43",
            "<b>Origin</b>: Single",
            "<b>Roast</b>: Light",
            hasEspresso = true,
            hasAeropress = false,
            hasColdBrew = true,
            hasPourOver = true,
            hasSyphon = false,
            hasImmersive = true,
            facebookUri = null,
            instagramUri = "",
            websiteUri = " "
    )
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