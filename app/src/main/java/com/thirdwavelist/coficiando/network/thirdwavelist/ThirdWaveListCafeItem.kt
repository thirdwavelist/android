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

package com.thirdwavelist.coficiando.network.thirdwavelist

import android.net.Uri
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class ThirdWaveListCafeItem(
    @SerializedName("uid") val id: UUID,
    @SerializedName("name") val name: String?,
    @SerializedName("title") val address: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("extra_thumbnail") val thumbnail: Uri?,
    @SerializedName("social_website") val socialWebsite: Uri?,
    @SerializedName("social_facebook") val socialFacebook: Uri?,
    @SerializedName("social_instagram") val socialInstagram: Uri?,
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
        (this.thumbnail != null && this.thumbnail.scheme.equals("https", ignoreCase = true))
}