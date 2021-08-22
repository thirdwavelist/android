package com.thirdwavelist.coficiando.core.dagger.modules

import com.thirdwavelist.coficiando.core.logging.BusinessEventLogger
import com.thirdwavelist.coficiando.core.logging.ErrorEventLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AnalyticsModule {

    @Provides
    @Singleton
    fun provideBusinessEventLogger(): BusinessEventLogger = BusinessEventLogger()

    @Provides
    @Singleton
    fun provideErrorEventLogger(): ErrorEventLogger = ErrorEventLogger()
}