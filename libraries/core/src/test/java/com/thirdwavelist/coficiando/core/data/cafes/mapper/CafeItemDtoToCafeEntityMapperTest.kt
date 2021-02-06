package com.thirdwavelist.coficiando.core.data.cafes.mapper

import com.squareup.burst.BurstJUnit4
import com.thirdwavelist.coficiando.core.data.network.model.CafeItemDto
import com.thirdwavelist.coficiando.core.domain.cafe.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatNullPointerException
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(BurstJUnit4::class)
class CafeItemDtoToCafeEntityMapperTest {

    private val mapper = CafeItemDtoToCafeEntityMapper()

    @Test
    fun `Given valid dto, when mapped, then expected entity is created`() {
        // Given
        val validInput = generateCafeItemDto()
        val expectedOutput = CafeItem(
                validInput.id,
                validInput.name!!,
                validInput.thumbnail.toString(),
                SocialItem(validInput.socialFacebook, validInput.socialInstagram, validInput.socialWebsite),
                validInput.googlePlaceId!!,
                GearInfoItem(validInput.gearEspressoMachine, validInput.gearGrinder),
                BeanInfoItem(validInput.beanOrigin, validInput.beanRoaster, validInput.beanOriginSingle!!, validInput.beanOriginBlend!!, validInput.beanRoastLight!!, validInput.beanRoastMedium!!, validInput.beanRoastDark!!),
                BrewInfoItem(validInput.doesServeEspresso!!, validInput.doesServeAeropress!!, validInput.doesServePourOver!!, validInput.doesServeColdBrew!!, validInput.doesServeSyphon!!, validInput.doesServeFullImmersive!!)
        )

        // When
        val entity = mapper(validInput)

        // Then
        assertThat(entity).usingRecursiveComparison().isEqualTo(expectedOutput)
    }

    @Test
    fun `Given invalid dto, when mapped, then returns NullPointerException`(invalidCasePermutation: InvalidCasePermutation) {
        // Given
        val invalidDto = invalidCasePermutation.invalidDto

        // When - Then
        assertThatNullPointerException().isThrownBy {
            mapper(invalidDto)
        }
    }

    enum class InvalidCasePermutation(val invalidDto: CafeItemDto) {
        MISSING_NAME(generateCafeItemDto(cafeName = null)),
        MISSING_THUMBNAIL(generateCafeItemDto(thumbnailUrl = null)),
        MISSING_GOOGLE_PLACE_ID(generateCafeItemDto(googlePlaceId = null))
    }

    companion object {
        fun generateCafeItemDto(
                cafeName: String? = "cafeName",
                thumbnailUrl: String? = "https://thumbnailUrl",
                googlePlaceId: String? = "googlePlaceId"
        ) = CafeItemDto(
                UUID.randomUUID(),
                cafeName,
                "cafeAddress",
                thumbnailUrl,
                "https://websiteUrl",
                "https://facebookPageUrl",
                "https://instagramUrl",
                doesServeEspresso = true,
                doesServeColdBrew = true,
                doesServePourOver = true,
                doesServeSyphon = false,
                doesServeAeropress = false,
                doesServeFullImmersive = true,
                "Rancilio",
                "Baratza",
                "V60",
                "French Press",
                "Ozone",
                "Ethiopia",
                beanOriginSingle = true,
                beanOriginBlend = false,
                beanRoastLight = true,
                beanRoastMedium = true,
                beanRoastDark = false,
                "3.44",
                googlePlaceId
        )

    }
}