package com.thirdwavelist.coficiando.details.di

import com.thirdwavelist.coficiando.core.di.CoreComponent
import dagger.Component

@Component(dependencies = [CoreComponent::class], modules = [DetailsModule::class])
interface DetailsComponent