package com.thirdwavelist.coficiando.navigation

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.thirdwavelist.coficiando.navigation.R
import kotlinx.parcelize.Parcelize

sealed class Destination(
    val route: String,
    val deepLinks: List<String>
): Parcelable {
    @Parcelize
    data class Home(@StringRes val titleRes: Int = R.string.nav_home_title) : Destination(
        "home",
        deepLinks = listOf("https://thirdwavelist.com"),
    )
    @Immutable
    @Parcelize
    data class Details(val id: String) : Destination(
        route = "details/${id}",
        deepLinks = listOf("https://thirdwavelist.com/cafe/${id}"),
    )
    @Parcelize
    data class Settings(@StringRes val titleRes: Int = R.string.nav_settings_title) : Destination(
        route = "settings",
        deepLinks = listOf("https://thirdwavelist.com/settings"),

    )
}

class Actions(navigator: Navigator<Destination>) {
    val navigateToDetails: (String) -> Unit = { detailsId: String ->
        navigator.navigate(Destination.Details(detailsId))
    }
    val navigateToSettings: () -> Unit = {
        navigator.navigate(Destination.Settings())
    }
    val navigateToHome: () -> Unit = {
        navigator.navigate(Destination.Home())
    }
    val upPress: () -> Unit = {
        navigator.back()
    }
}