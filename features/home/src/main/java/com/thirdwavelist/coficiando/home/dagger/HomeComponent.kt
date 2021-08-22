package com.thirdwavelist.coficiando.home.dagger

import com.thirdwavelist.coficiando.core.dagger.CoreComponent
import dagger.Component

@Component(dependencies = [CoreComponent::class], modules = [HomeModule::class])
interface HomeComponent