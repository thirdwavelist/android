package com.thirdwavelist.coficiando.core.di.modules

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.thirdwavelist.coficiando.core.R
import com.thirdwavelist.coficiando.core.data.network.CachePreference
import com.thirdwavelist.coficiando.core.di.qualifiers.ApiKey
import com.thirdwavelist.coficiando.core.di.qualifiers.CacheDuration
import com.thirdwavelist.coficiando.core.di.qualifiers.CacheSize
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ConfigurationModule {

    private const val CONFIG_KEY_API = "key_api"
    private const val CACHE_SIZE_1MB = 1024 * 1024L
    private const val CACHE_DURATION_ONE_DAY_IN_SECONDS = 60 * 60 * 24L

    @Provides
    @Singleton
    internal fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        return FirebaseRemoteConfig.getInstance().also { config ->
            config.setDefaultsAsync(R.xml.default_app_config)
            config.fetch()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) config.fetchAndActivate()
                    }
        }
    }

    @Provides
    @Singleton
    @ApiKey
    fun provideApiKey(appConfig: FirebaseRemoteConfig): String {
        return appConfig.getString(CONFIG_KEY_API)
    }

    @Provides
    @Singleton
    @CacheDuration
    fun provideCacheDuration() = CACHE_DURATION_ONE_DAY_IN_SECONDS

    @Provides
    @Singleton
    @CacheSize
    fun provideCacheSize() = CACHE_SIZE_1MB

    @Provides
    @Singleton
    fun provideCachePreference(): CachePreference =
            CachePreference("public, only-if-cached, max-stale=$CACHE_DURATION_ONE_DAY_IN_SECONDS",
                    "public, max-age=$CACHE_DURATION_ONE_DAY_IN_SECONDS")
}