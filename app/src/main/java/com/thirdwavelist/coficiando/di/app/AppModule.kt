package com.thirdwavelist.coficiando.di.app

import android.content.Context
import com.thirdwavelist.coficiando.MainApplication
import com.thirdwavelist.coficiando.di.qualifiers.AppContext
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {
    @AppContext
    @Binds
    abstract fun bindsContext(app: MainApplication): Context
}