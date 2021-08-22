package com.thirdwavelist.coficiando.dagger

import com.thirdwavelist.coficiando.core.dagger.CoreComponent
import com.thirdwavelist.coficiando.details.dagger.DetailsComponent
import com.thirdwavelist.coficiando.home.dagger.HomeComponent
import com.thirdwavelist.coficiando.navigation.Destination
import com.thirdwavelist.coficiando.navigation.dagger.NavigationComponent
import com.thirdwavelist.coficiando.settings.dagger.SettingsComponent
import dagger.Component
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
@Component(
    dependencies = [
        CoreComponent::class,
        HomeComponent::class,
        DetailsComponent::class,
        SettingsComponent::class,
        NavigationComponent::class,
    ],
)
interface AppComponent