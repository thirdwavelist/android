package com.thirdwavelist.coficiando.core.di.modules

import com.thirdwavelist.coficiando.core.coroutines.AndroidDispatcherProvider
import com.thirdwavelist.coficiando.coreutils.coroutines.CoroutineDispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {
    @Provides
    fun provideDispatcherProvider(): CoroutineDispatcherProvider = AndroidDispatcherProvider()
}