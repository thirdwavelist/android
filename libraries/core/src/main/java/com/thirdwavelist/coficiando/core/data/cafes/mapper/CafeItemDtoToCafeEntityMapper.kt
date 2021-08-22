package com.thirdwavelist.coficiando.core.data.cafes.mapper

import com.thirdwavelist.coficiando.core.domain.cafe.*
import com.thirdwavelist.coficiando.core.data.network.model.CafeItemDto
import com.thirdwavelist.coficiando.core.util.ext.Mapper
import javax.inject.Inject

class CafeItemDtoToCafeEntityMapper @Inject constructor() : Mapper<CafeItemDto, Cafe> {

    override fun invoke(from: CafeItemDto): Cafe =
            Cafe(id = from.id,
                    name = from.name!!,
                    thumbnail = from.thumbnail!!.toString(),
                    social = SocialItem(facebookUri = from.socialFacebook,
                            instagramUri = from.socialInstagram,
                            homepageUri = from.socialWebsite),
                    googlePlaceId = from.googlePlaceId!!,
                    gearInfo = GearInfoItem(espressoMachineName = from.gearEspressoMachine,
                            grinderMachineName = from.gearGrinder),
                    beanInfo = BeanInfoItem(origin = from.beanOrigin,
                            roaster = from.beanRoaster,
                            hasSingleOrigin = from.beanOriginSingle ?: false,
                            hasBlendOrigin = from.beanOriginBlend ?: false,
                            hasLightRoast = from.beanRoastLight ?: false,
                            hasMediumRoast = from.beanRoastMedium ?: false,
                            hasDarkRoast = from.beanRoastDark ?: false),
                    brewInfo = BrewInfoItem(hasEspresso = from.doesServeEspresso
                            ?: false,
                            hasAeropress = from.doesServeAeropress ?: false,
                            hasColdBrew = from.doesServeColdBrew ?: false,
                            hasFullImmersive = from.doesServeFullImmersive ?: false,
                            hasPourOver = from.doesServePourOver ?: false,
                            hasSyphon = from.doesServeSyphon ?: false)
            )
}