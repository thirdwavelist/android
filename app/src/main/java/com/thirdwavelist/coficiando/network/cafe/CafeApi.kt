package com.thirdwavelist.coficiando.network.cafe

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.PropertyName
import com.thirdwavelist.coficiando.network.shared.CafeId
import com.thirdwavelist.coficiando.network.shared.CityId
import io.reactivex.Single

@Keep
data class CafeDto(
    @Exclude val id: String = "",
    @PropertyName("name") val name: String = "",
    @PropertyName("address") val address: String = "",
    @PropertyName("city") val city: DocumentReference? = null,
    @PropertyName("google_place_id") val googlePlaceId: String = "",
    @PropertyName("instagram_id") val instagramId: String = "",
    @PropertyName("location") val location: GeoPoint = GeoPoint(0.0, 0.0)
)

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
                .addOnCompleteListener { task ->
                    val response = task.result?.documents
                    if (task.isSuccessful && response != null) {
                        val results = mutableListOf<CafeDto>()
                        for (item in response) {
                            item.toObject(CafeDto::class.java)?.copy(id = item.id)?.let {
                                results.plusAssign(it)
                            }
                        }
                        emitter.onSuccess(results)
                    } else if (task.isCanceled || !task.isSuccessful) {
                        emitter.onError(Throwable(task.exception))
                    }
                }
                .addOnFailureListener {
                    emitter.onError(Throwable(it))
                }
        }
    }

    override fun getCafes(cityId: CityId): Single<List<CafeDto>> {
        return Single.create { emitter ->
            db.getCafeFor(cityId)
                .addOnCompleteListener { task ->
                    val response = task.result?.documents
                    if (task.isSuccessful && response != null) {
                        val results = mutableListOf<CafeDto>()
                        for (item in response) {
                            item.toObject(CafeDto::class.java)?.copy(id = item.id)?.let {
                                results.plusAssign(it)
                            }                        }
                        emitter.onSuccess(results)
                    } else if (task.isCanceled || !task.isSuccessful) {
                        emitter.onError(Throwable(task.exception))
                    }
                }
                .addOnFailureListener {
                    emitter.onError(Throwable(it))
                }
        }
    }

    override fun getCafe(cafeId: CafeId): Single<CafeDto> {
        return Single.create { emitter ->
            db.getCafe(cafeId)
                .addOnCompleteListener { task ->
                    val response = task.result
                    if (task.isSuccessful && response != null) {
                        val result = response.toObject(CafeDto::class.java)!!.copy(id = response.id)
                        emitter.onSuccess(result)
                    } else if (task.isCanceled || !task.isSuccessful) {
                        emitter.onError(Throwable(task.exception))
                    }
                }
                .addOnFailureListener {
                    emitter.onError(Throwable(it))
                }
        }
    }

    private fun FirebaseFirestore.getCafeFor(cityId: CityId) = this.collection("cafe").whereEqualTo("city", cityId.id).get()
    private fun FirebaseFirestore.getCafes() = this.collection("cafe").get()
    private fun FirebaseFirestore.getCafe(cafeId: CafeId) = this.collection("cafe").document(cafeId.id).get()
}