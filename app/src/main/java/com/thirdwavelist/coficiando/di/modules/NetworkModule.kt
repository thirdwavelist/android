package com.thirdwavelist.coficiando.di.modules

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.thirdwavelist.coficiando.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import javax.inject.Singleton
import android.net.ConnectivityManager
import android.net.Uri
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.thirdwavelist.coficiando.di.qualifiers.AppContext
import com.thirdwavelist.coficiando.network.CachePreference


@Module(includes = [CacheModule::class])
object NetworkModule {
    private const val USER_AGENT = "User-Agent"
    private const val HEADER_CACHE_CONTROL = "Cache-Control"

    @Provides
    @Singleton
    @JvmStatic
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
    @JvmStatic
    internal fun provideOkHttpClient(@AppContext context: Context, cache: Cache, cachePreference: CachePreference): OkHttpClient {
        return OkHttpClient.Builder()
            .debuggable(BuildConfig.DEBUG)
            .userAgent(BuildConfig.APPLICATION_ID)
            .cachePreference(context, cachePreference)
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

    private fun OkHttpClient.Builder.cachePreference(@AppContext context: Context, cachePreference: CachePreference): OkHttpClient.Builder {
        addNetworkInterceptor { interceptor ->
            interceptor.proceed(interceptor.request()).newBuilder().apply {
                if (!context.isNetworkAvailable()) {
                    this.header(HEADER_CACHE_CONTROL, cachePreference.offlineCacheSettings)
                } else {
                    this.header(HEADER_CACHE_CONTROL, cachePreference.onlineCacheSettings)
                }
            }.build()
        }
        return this
    }

    private fun Context.isNetworkAvailable(): Boolean {
        (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).let {
            return it.activeNetworkInfo != null && it.activeNetworkInfo.isConnectedOrConnecting
        }
    }
}