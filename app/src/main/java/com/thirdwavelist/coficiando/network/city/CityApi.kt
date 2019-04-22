package com.thirdwavelist.coficiando.network.city

import androidx.annotation.Keep
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.PropertyName
import com.thirdwavelist.coficiando.network.shared.CityId
import io.reactivex.Single

@Keep
data class CityDto(
    @Exclude val id: String = "",
    @PropertyName("city_name") val cityName: String = "",
    @PropertyName("country_code") val countryCode: String = "",
    @PropertyName("location") val location: GeoPoint = GeoPoint(0.0, 0.0)
)

interface CityApi {
    fun getCities(): Single<List<CityDto>>
    fun getCity(cityId: CityId): Single<CityDto>
}

internal class CityApiImpl : CityApi {
    val db = FirebaseFirestore.getInstance()

    override fun getCities(): Single<List<CityDto>> {
        return Single.create { emitter ->
            db.getCities()
                .addOnCompleteListener { task ->
                    val response = task.result?.documents
                    if (task.isSuccessful && response != null) {
                        val results = mutableListOf<CityDto>()
                        response.forEach { item ->
                            item.toObject(CityDto::class.java)?.copy(id = item.id)?.let {
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

    override fun getCity(cityId: CityId): Single<CityDto> {
        return Single.create { emitter ->
            db.getCity(cityId)
                .addOnCompleteListener { task ->
                    val response = task.result
                    if (task.isSuccessful && response != null) {
                        val result = response.toObject(CityDto::class.java)!!.copy(id = response.id)
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

    private fun FirebaseFirestore.getCity(cityId: CityId) = this.collection("city").document(cityId.id).get()
    private fun FirebaseFirestore.getCities() = this.collection("city").get()
}