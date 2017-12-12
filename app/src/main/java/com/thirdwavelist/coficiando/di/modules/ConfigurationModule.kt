package com.thirdwavelist.coficiando.di.modules

import dagger.Module
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.thirdwavelist.coficiando.R
import com.thirdwavelist.coficiando.di.qualifiers.ApiKey
import com.thirdwavelist.coficiando.di.qualifiers.CacheDuration
import com.thirdwavelist.coficiando.di.qualifiers.CachePreference
import com.thirdwavelist.coficiando.di.qualifiers.CacheSize
import dagger.Provides
import javax.inject.Singleton

@Module
object ConfigurationModule {

    private const val CONFIG_KEY_API = "key_api"
    private const val CACHE_SIZE_1MB = 1024 * 1024L
    private const val CACHE_DURATION_ONE_DAY_IN_SECONDS = 60 * 60 * 24L
    private const val PUBLIC_CACHE_PREFERENCE = "public, only-if-cached"
    
    @Provides
    @JvmStatic
    @Singleton
    internal fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        return FirebaseRemoteConfig.getInstance().also { config ->
            config.setDefaults(R.xml.default_app_config)
            config.fetch()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) config.activateFetched()
                }
        }
    }

    @Provides
    @Singleton
    @JvmStatic
    @ApiKey
    fun provideApiKey(appConfig: FirebaseRemoteConfig): String {
        return appConfig.getString(CONFIG_KEY_API)
    }

    @Provides
    @JvmStatic
    @Singleton
    @CacheDuration
    fun provideCacheDuration() = CACHE_DURATION_ONE_DAY_IN_SECONDS

    @Provides
    @JvmStatic
    @Singleton
    @CacheSize
    fun provideCacheSize() = CACHE_SIZE_1MB

    @Provides
    @JvmStatic
    @Singleton
    @CachePreference
    fun provideCachePreference() = PUBLIC_CACHE_PREFERENCE
}