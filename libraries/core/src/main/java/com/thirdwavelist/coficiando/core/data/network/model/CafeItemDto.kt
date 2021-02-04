package com.thirdwavelist.coficiando.core.data.network.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class CafeItemDto(
        @SerializedName("uid") val id: UUID,
        @SerializedName("name") val name: String?,
        @SerializedName("title") val address: String?,
        @SerializedName("extra_thumbnail") val thumbnail: String?,
        @SerializedName("social_website") val socialWebsite: String?,
        @SerializedName("social_facebook") val socialFacebook: String?,
        @SerializedName("social_instagram") val socialInstagram: String?,
        @SerializedName("brew_method_espresso") val doesServeEspresso: Boolean?,
        @SerializedName("brew_method_coldbrew") val doesServeColdBrew: Boolean?,
        @SerializedName("brew_method_pourover") val doesServePourOver: Boolean?,
        @SerializedName("brew_method_syphon") val doesServeSyphon: Boolean?,
        @SerializedName("brew_method_aeropress") val doesServeAeropress: Boolean?,
        @SerializedName("brew_method_fullimmersion") val doesServeFullImmersive: Boolean?,
        @SerializedName("gear_espressomachine") val gearEspressoMachine: String?,
        @SerializedName("gear_grinder") val gearGrinder: String?,
        @SerializedName("gear_pourover") val gearPourOver: String?,
        @SerializedName("gear_immersive") val gearFullImmersive: String?,
        @SerializedName("bean_roaster") val beanRoaster: String?,
        @SerializedName("bean_country") val beanOrigin: String?,
        @SerializedName("bean_origin_single") val beanOriginSingle: Boolean?,
        @SerializedName("bean_origin_blend") val beanOriginBlend: Boolean?,
        @SerializedName("bean_roast_light") val beanRoastLight: Boolean?,
        @SerializedName("bean_roast_medium") val beanRoastMedium: Boolean?,
        @SerializedName("bean_roast_dark") val beanRoastDark: Boolean?,
        @SerializedName("price_doppio") val priceDoppio: String?,
        @SerializedName("extra_google_placeid") val googlePlaceId: String?
) {
    fun isValid() = !this.name.isNullOrBlank() &&
            (this.thumbnail != null && this.thumbnail.startsWith("https", ignoreCase = true))
}