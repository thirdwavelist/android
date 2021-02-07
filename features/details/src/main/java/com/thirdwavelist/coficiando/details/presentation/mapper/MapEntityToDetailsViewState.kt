package com.thirdwavelist.coficiando.details.presentation.mapper

import com.thirdwavelist.coficiando.core.di.qualifiers.GoogleMapsLink
import com.thirdwavelist.coficiando.core.domain.cafe.BeanInfoItem.Companion.availableOriginTypes
import com.thirdwavelist.coficiando.core.domain.cafe.BeanInfoItem.Companion.availableRoastTypes
import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem
import com.thirdwavelist.coficiando.coreutils.ext.Mapper
import com.thirdwavelist.coficiando.details.presentation.DetailsViewState
import javax.inject.Inject

class MapEntityToDetailsViewState @Inject constructor(
        @GoogleMapsLink private val googleMapsUrl: String
) : Mapper<CafeItem, DetailsViewState> {
    override fun invoke(from: CafeItem) = DetailsViewState.Success(
            name = from.name,
            thumbnailUrl = from.thumbnail,
            googleMapsUri = composeGoogleMapsUrl(from.googlePlaceId),
            espressoMachineName = from.gearInfo.espressoMachineName,
            grinderMachineName = from.gearInfo.grinderMachineName,
            availableBeanOrigin = from.beanInfo.availableOriginTypes(),
            availableBeanRoast = from.beanInfo.availableRoastTypes(),
            hasEspresso = from.brewInfo.hasEspresso,
            hasAeropress = from.brewInfo.hasAeropress,
            hasColdBrew = from.brewInfo.hasColdBrew,
            hasPourOver = from.brewInfo.hasPourOver,
            hasSyphon = from.brewInfo.hasSyphon,
            hasImmersive = from.brewInfo.hasFullImmersive,
            facebookUri = from.social.facebookUri,
            instagramUri = from.social.instagramUri,
            websiteUri = from.social.homepageUri,
    )

    private fun composeGoogleMapsUrl(googlePlaceId: String) = "${googleMapsUrl}${googlePlaceId}"
}