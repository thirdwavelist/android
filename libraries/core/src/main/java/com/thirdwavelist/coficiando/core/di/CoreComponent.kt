package com.thirdwavelist.coficiando.core.di

import com.thirdwavelist.coficiando.core.di.modules.ServiceModule
import com.thirdwavelist.coficiando.core.di.modules.StorageModule
import dagger.Component

@Component(modules = [ServiceModule::class, StorageModule::class])
interface CoreComponent