package com.thirdwavelist.coficiando.core.dagger.modules

import com.thirdwavelist.coficiando.core.coroutines.AndroidDispatcherProvider
import com.thirdwavelist.coficiando.core.util.coroutines.CoroutineDispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object CoroutinesModule {
    @Provides
    fun provideDispatcherProvider(): CoroutineDispatcherProvider = AndroidDispatcherProvider()
}