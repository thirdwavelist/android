@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package com.thirdwavelist.coficiando.network.cafe

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.model.value.GeoPointValue
import com.google.firebase.firestore.model.value.ReferenceValue
import com.google.firebase.firestore.model.value.StringValue
import io.reactivex.Single

data class CafeDto(
    val name: StringValue,
    val address: StringValue,
    val city: ReferenceValue,
    val googlePlacesId: StringValue,
    val instagramId: StringValue,
    val location: GeoPointValue
)

inline class CafeId(val id: String)
inline class CityId(val id: String)

interface CafeApi {
    fun getCafes(): Single<List<CafeDto>>
    fun getCafes(cityId: CityId): Single<List<CafeDto>>
    fun getCafe(cafeId: CafeId): Single<CafeDto>
}

internal class CafeApiImpl : CafeApi {
    val db = FirebaseFirestore.getInstance()

    override fun getCafes(): Single<List<CafeDto>> {
        return Single.just(
            db.getCafes()
                .result?.toObjects(CafeDto::class.java)
        )
    }

    override fun getCafes(cityId: CityId): Single<List<CafeDto>> {
        return Single.just(
            db.getCafeFor(cityId)
                .result?.toObjects(CafeDto::class.java)
        )
    }

    override fun getCafe(cafeId: CafeId): Single<CafeDto> {
        return Single.just(
            db.getCafe(cafeId)
                .result?.toObject(CafeDto::class.java)
        )
    }

    private fun FirebaseFirestore.getCafeFor(cityId: CityId) = this.collection("cafe").whereEqualTo("city", cityId.id).get()
    private fun FirebaseFirestore.getCafes() = this.collection("cafe").get()
    private fun FirebaseFirestore.getCafe(cafeId: CafeId) = this.collection("cafe").document(cafeId.id).get()
}