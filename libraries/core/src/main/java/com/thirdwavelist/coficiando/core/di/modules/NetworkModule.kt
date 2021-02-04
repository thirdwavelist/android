package com.thirdwavelist.coficiando.core.di.modules

import android.content.Context
import android.net.Uri
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.thirdwavelist.coficiando.core.BuildConfig
import com.thirdwavelist.coficiando.core.data.network.CachePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import javax.inject.Singleton

@Module(includes = [CacheModule::class])
@InstallIn(ApplicationComponent::class)
object NetworkModule {
    private const val USER_AGENT = "User-Agent"
    private const val HEADER_CACHE_CONTROL = "Cache-Control"

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapter(Uri::class.java, object : TypeAdapter<Uri?>() {
                    override fun write(to: JsonWriter, value: Uri?) {
                        to.value(value.toString());
                    }

                    override fun read(from: JsonReader): Uri? {
                        if (from.peek() == JsonToken.NULL) {
                            from.nextNull();
                            return null;
                        }
                        from.nextString().let {
                            return if (it.isEmpty()) Uri.EMPTY else Uri.parse(it);
                        }
                    }
                })
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(@ApplicationContext context: Context, cache: Cache, cachePreference: CachePreference): OkHttpClient {
        return OkHttpClient.Builder()
                .debuggable(BuildConfig.DEBUG)
                .userAgent("Coficiando/Android")
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
                        .method(request.method, request.body)
                        .build()
            })
        }
        return this
    }

    private fun OkHttpClient.Builder.cachePreference(cachePreference: CachePreference): OkHttpClient.Builder {
        addNetworkInterceptor { interceptor ->
            interceptor.proceed(interceptor.request()).newBuilder().apply {
                this.header(HEADER_CACHE_CONTROL, cachePreference.onlineCacheSettings)
            }.build()
        }
        return this
    }
}