package com.thirdwavelist.coficiando.di

import com.thirdwavelist.coficiando.core.di.CoreComponent
import com.thirdwavelist.coficiando.details.di.DetailsComponent
import com.thirdwavelist.coficiando.home.di.HomeComponent
import com.thirdwavelist.coficiando.settings.di.SettingsComponent
import dagger.Component
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
@Component(dependencies = [
    CoreComponent::class,
    HomeComponent::class,
    DetailsComponent::class,
    SettingsComponent::class
])
interface AppComponent