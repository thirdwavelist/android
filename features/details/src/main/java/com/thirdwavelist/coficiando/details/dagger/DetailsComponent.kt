package com.thirdwavelist.coficiando.details.dagger

import com.thirdwavelist.coficiando.core.dagger.CoreComponent
import dagger.Component

@Component(dependencies = [CoreComponent::class], modules = [DetailsModule::class])
interface DetailsComponent