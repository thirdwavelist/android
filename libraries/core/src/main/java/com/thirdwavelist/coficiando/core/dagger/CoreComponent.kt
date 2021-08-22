package com.thirdwavelist.coficiando.core.dagger

import com.thirdwavelist.coficiando.core.dagger.modules.AnalyticsModule
import com.thirdwavelist.coficiando.core.dagger.modules.CoroutinesModule
import com.thirdwavelist.coficiando.core.dagger.modules.ServiceModule
import com.thirdwavelist.coficiando.core.dagger.modules.StorageModule
import dagger.Component

@Component(modules = [
    ServiceModule::class,
    StorageModule::class,
    CoroutinesModule::class,
    AnalyticsModule::class
])
interface CoreComponent