package com.thirdwavelist.coficiando.di.app

import com.thirdwavelist.coficiando.MainApplication
import com.thirdwavelist.coficiando.di.ActivityBuilderModule
import com.thirdwavelist.coficiando.di.modules.ServiceModule
import com.thirdwavelist.coficiando.di.modules.StorageModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, ActivityBuilderModule::class, ServiceModule::class, StorageModule::class])
interface AppComponent : AndroidInjector<MainApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MainApplication>()
}