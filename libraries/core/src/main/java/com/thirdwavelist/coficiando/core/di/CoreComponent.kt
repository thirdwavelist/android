package com.thirdwavelist.coficiando.core.di

import com.thirdwavelist.coficiando.core.di.modules.ServiceModule
import com.thirdwavelist.coficiando.core.di.modules.CoroutinesModule
import com.thirdwavelist.coficiando.core.di.modules.StorageModule
import dagger.Component

@Component(modules = [ServiceModule::class, StorageModule::class, CoroutinesModule::class])
interface CoreComponent