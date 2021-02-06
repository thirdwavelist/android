package com.thirdwavelist.coficiando.navigation

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.then
import org.junit.Test

class NavigatorTest {

    private val mockNavController: NavController = mock()
    private val mockFragmentNavigatorExtrasMapper: (Array<Pair<View, String>>) -> FragmentNavigator.Extras = mock()
    private val mockNavigatorExtras: FragmentNavigator.Extras = mock()

    private val navigator: Navigator = Navigator(mockNavController, mockFragmentNavigatorExtrasMapper)

    @Test
    fun `Given Home navigation flow When #navigateToFlow() Then navigates to home`() {
        // Given
        val extras = emptyArray<Pair<View, String>>()
        given(mockFragmentNavigatorExtrasMapper(extras)).willReturn(mockNavigatorExtras)

        // When
        navigator.navigateToFlow(NavigationFlow.HomeFlow, emptyArray())

        // Then
        then(mockNavController).should().navigate(NavMainDirections.actionGlobalHomeFlow(), mockNavigatorExtras)
    }

    @Test
    fun `Given Details navigation flow When #navigateToFlow() Then navigates to details with correct parameters`() {
        // Given
        val extras = emptyArray<Pair<View, String>>()
        given(mockFragmentNavigatorExtrasMapper(extras)).willReturn(mockNavigatorExtras)
        val detailsId = "detailsId"

        // When
        navigator.navigateToFlow(NavigationFlow.DetailsFlow(detailsId), emptyArray())

        // Then
        then(mockNavController).should().navigate(NavMainDirections.actionGlobalDetailsFlow(detailsId), mockNavigatorExtras)
    }
}