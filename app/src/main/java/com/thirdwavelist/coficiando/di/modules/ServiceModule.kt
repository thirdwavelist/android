/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2017, Antal János Monori
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.thirdwavelist.coficiando.di.modules

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.thirdwavelist.coficiando.di.qualifiers.ApiKey
import com.thirdwavelist.coficiando.network.thirdwavelist.ThirdWaveListService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, ConfigurationModule::class])
object ServiceModule {

    private const val KEY_ENDPOINT_THIRD_WAVE_LIST = "endpoint_thirdwavelistapi";
    private const val HEADER_API_KEY = "x-api-key"

    @Provides
    @Singleton
    @JvmStatic
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