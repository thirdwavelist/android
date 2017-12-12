package com.thirdwavelist.coficiando.di.modules

import android.content.Context
import com.thirdwavelist.coficiando.di.qualifiers.AppContext
import com.thirdwavelist.coficiando.di.qualifiers.CacheDuration
import com.thirdwavelist.coficiando.di.qualifiers.CachePreference
import com.thirdwavelist.coficiando.di.qualifiers.CacheSize
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import java.io.File
import javax.inject.Singleton

@Module(includes = [ConfigurationModule::class])
object CacheModule {
    private const val NETWORK_CACHE_FILE = "cache/http-cache"

    @Provides
    @JvmStatic
    internal fun provideHttpCache(@AppContext context: Context, @CacheSize cacheSize: Long): Cache =
        Cache(File(context.cacheDir, NETWORK_CACHE_FILE), cacheSize)
}