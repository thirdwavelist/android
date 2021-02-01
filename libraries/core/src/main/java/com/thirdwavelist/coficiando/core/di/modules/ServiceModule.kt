package com.thirdwavelist.coficiando.core.di.modules

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.thirdwavelist.coficiando.core.di.qualifiers.ApiKey
import com.thirdwavelist.coficiando.core.network.thirdwavelist.ThirdWaveListService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, ConfigurationModule::class])
@InstallIn(ApplicationComponent::class)
object ServiceModule {

    private const val KEY_ENDPOINT_THIRD_WAVE_LIST = "endpoint_thirdwavelistapi";
    private const val HEADER_API_KEY = "x-api-key"

    @Provides
    @Singleton
    internal fun provideThirdWaveListService(appConfig: FirebaseRemoteConfig, okHttp: OkHttpClient, gSon: Gson, @ApiKey apiKey: String): ThirdWaveListService {
        return Retrofit.Builder()
            .baseUrl(appConfig, KEY_ENDPOINT_THIRD_WAVE_LIST)
            .client(okHttp.newBuilder().apiKey(apiKey).build())
            .addConverterFactory(GsonConverterFactory.create(gSon))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ThirdWaveListService::class.java)
    }

    private fun Retrofit.Builder.baseUrl(appConfig: FirebaseRemoteConfig, endpointKey: String): Retrofit.Builder {
        (appConfig.getString(endpointKey)).let {
            baseUrl(it)
        }
        return this
    }

    private fun OkHttpClient.Builder.apiKey(@ApiKey apiKey: String): OkHttpClient.Builder {
        addInterceptor { interceptor ->
            interceptor.proceed(interceptor.request().let { request ->
                request.newBuilder()
                    .header(HEADER_API_KEY, apiKey)
                    .method(request.method(), request.body())
                    .build()
            })
        }
        return this
    }
}