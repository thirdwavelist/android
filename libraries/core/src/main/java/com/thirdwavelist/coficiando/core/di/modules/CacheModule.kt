package com.thirdwavelist.coficiando.core.di.modules

import android.content.Context
import com.thirdwavelist.coficiando.core.di.qualifiers.CacheSize
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import java.io.File

@Module(includes = [ConfigurationModule::class])
@InstallIn(SingletonComponent::class)
internal object CacheModule {
    private const val NETWORK_CACHE_FILE = "cache/http-cache"

    @Provides
    internal fun provideHttpCache(@ApplicationContext context: Context, @CacheSize cacheSize: Long): Cache =
            Cache(File(context.cacheDir, NETWORK_CACHE_FILE), cacheSize)
}