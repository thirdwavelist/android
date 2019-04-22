@file:Suppress("EXPERIMENTAL_FEATURE_WARNING")

package com.thirdwavelist.coficiando.network.cafe

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.PropertyName
import io.reactivex.Single
import java.util.UUID

@Keep
data class CafeDto(
    @Exclude val id: String = UUID.randomUUID().toString(),
    @PropertyName("name") val name: String = "",
    @PropertyName("address") val address: String = "",
    @PropertyName("city") val city: DocumentReference? = null,
    @PropertyName("google_place_id") val googlePlacesId: String = "",
    @PropertyName("instagram_id") val instagramId: String = "",
    @PropertyName("location") val location: GeoPoint = GeoPoint(0.0, 0.0)
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
        return Single.create { emitter ->
            db.getCafes()
                .addOnCompleteListener {
                    val results = it.result?.toObjects(CafeDto::class.java)
                    if (results != null) {
                        emitter.onSuccess(results)
                    } else {
                        emitter.onError(Throwable(it.exception))
                    }
                }
        }
    }

    override fun getCafes(cityId: CityId): Single<List<CafeDto>> {
        return Single.create { emitter ->
            db.getCafeFor(cityId)
                .addOnCompleteListener {
                    val results = it.result?.toObjects(CafeDto::class.java)
                    if (results != null) {
                        emitter.onSuccess(results)
                    } else {
                        emitter.onError(Throwable(it.exception))
                    }
                }
        }
    }

    override fun getCafe(cafeId: CafeId): Single<CafeDto> {
        return Single.create { emitter ->
            db.getCafe(cafeId)
                .addOnCompleteListener {
                    val results = it.result?.toObject(CafeDto::class.java)
                    if (results != null) {
                        emitter.onSuccess(results)
                    } else {
                        emitter.onError(Throwable(it.exception))
                    }
                }
        }
    }

    private fun FirebaseFirestore.getCafeFor(cityId: CityId) = this.collection("cafe").whereEqualTo("city", cityId.id).get()
    private fun FirebaseFirestore.getCafes() = this.collection("cafe").get()
    private fun FirebaseFirestore.getCafe(cafeId: CafeId) = this.collection("cafe").document(cafeId.id).get()
}