package com.thirdwavelist.coficiando.di

import com.thirdwavelist.coficiando.core.di.CoreComponent
import dagger.Component
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@EntryPoint
@InstallIn(ApplicationComponent::class)
@Component(dependencies = [CoreComponent::class])
interface AppComponent