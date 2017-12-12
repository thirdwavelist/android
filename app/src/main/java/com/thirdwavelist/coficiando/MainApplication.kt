package com.thirdwavelist.coficiando

import com.thirdwavelist.coficiando.di.app.AppComponent
import com.thirdwavelist.coficiando.di.app.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class MainApplication: DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<MainApplication>
        = DaggerAppComponent.builder().create(this)
}