package com.thirdwavelist.coficiando.network.thirdwavelist

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface ThirdWaveListService {

    @Headers("Accept: application/json")
    @GET("/v1/cafe")
    fun getCafes(): Single<List<ThirdWaveListCafeItem>>

    @Headers("Accept: application/json")
    @POST("/v1/cafe")
    fun postCafe(@Body cafeItem: ThirdWaveListCafeItem)

    @Headers("Accept: application/json")
    @GET("/v1/cafe/{uid}")
    fun getCafe(@Path("uid") id: UUID): Single<ThirdWaveListCafeItem>

    @Headers("Accept: application/json")
    @DELETE("/v1/cafe/{uid}")
    fun deleteCafe(@Path("uid") id: UUID, @Body cafeItem: ThirdWaveListCafeItem)
}