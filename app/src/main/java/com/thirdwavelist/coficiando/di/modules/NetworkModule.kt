package com.thirdwavelist.coficiando.di.modules

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.thirdwavelist.coficiando.BuildConfig
import com.thirdwavelist.coficiando.di.qualifiers.CachePreference
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import javax.inject.Singleton

@Module(includes = [CacheModule::class])
object NetworkModule {
    private const val USER_AGENT = "User-Agent"
    private const val HEADER_CACHE_CONTROL = "Cache-Control"

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    @Provides
    @Singleton
    @JvmStatic
    internal fun provideOkHttpClient(cache: Cache, @CachePreference cachePreference: String): OkHttpClient {
        return OkHttpClient.Builder()
            .debuggable(BuildConfig.DEBUG)
            .userAgent(BuildConfig.APPLICATION_ID)
            .cachePreference(cachePreference)
            .cache(cache)
            .build()
    }

    private fun OkHttpClient.Builder.debuggable(debugEnabled: Boolean): OkHttpClient.Builder {
        addInterceptor(HttpLoggingInterceptor().apply {
            level = if (debugEnabled) Level.BODY else Level.NONE
        })
        return this
    }

    private fun OkHttpClient.Builder.userAgent(userAgent: String): OkHttpClient.Builder {
        addInterceptor { interceptor ->
            interceptor.proceed(interceptor.request().let { request ->
                request.newBuilder()
                    .header(USER_AGENT, userAgent)
                    .method(request.method(), request.body())
                    .build()
            })
        }
        return this
    }

    private fun OkHttpClient.Builder.cachePreference(@CachePreference cachePreference: String): OkHttpClient.Builder {
        addInterceptor { interceptor ->
            interceptor.proceed(interceptor.request().let { request ->
                request.newBuilder()
                    .header(HEADER_CACHE_CONTROL, cachePreference)
                    .method(request.method(), request.body())
                    .build()
            })
        }
        return this
    }
}