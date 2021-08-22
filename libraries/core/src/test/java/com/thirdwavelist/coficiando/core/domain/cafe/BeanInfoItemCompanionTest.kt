package com.thirdwavelist.coficiando.core.domain.cafe

import com.squareup.burst.BurstJUnit4
import com.thirdwavelist.coficiando.coroutines.cafe.BeanInfoItem.Companion.availableOriginTypes
import com.thirdwavelist.coficiando.coroutines.cafe.BeanInfoItem.Companion.availableRoastTypes
import com.thirdwavelist.coficiando.core.domain.cafe.BeanInfoItem
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(BurstJUnit4::class)
class BeanInfoItemCompanionTest {

    @Test
    fun `Given valid entity, when available origin types requested, then expected string is returned`(originTypePermutations: OriginTypePermutations) {
        // When
        val result = originTypePermutations.entity.availableOriginTypes()

        // Then
        assertThat(result).isEqualTo(originTypePermutations.expectedOutput)
    }

    enum class OriginTypePermutations(val entity: BeanInfoItem, val expectedOutput: String) {
        ALL(
                BeanInfoItem("Country", "Roaster", hasSingleOrigin = true, hasBlendOrigin = true, hasLightRoast = true, hasMediumRoast = true, hasDarkRoast = true),
                "<b>Origin</b>: Single, Blend"
        ),
        NONE(
                BeanInfoItem("Country", "Roaster", hasSingleOrigin = false, hasBlendOrigin = false, hasLightRoast = true, hasMediumRoast = true, hasDarkRoast = true),
                "<b>Origin</b>: Information not available"
        ),
        SINGLE_ORIGIN(
                BeanInfoItem("Country", "Roaster", hasSingleOrigin = true, hasBlendOrigin = false, hasLightRoast = true, hasMediumRoast = true, hasDarkRoast = true),
                "<b>Origin</b>: Single"
        ),
        BLEND(
                BeanInfoItem("Country", "Roaster", hasSingleOrigin = false, hasBlendOrigin = true, hasLightRoast = true, hasMediumRoast = true, hasDarkRoast = true),
                "<b>Origin</b>: Blend"
        )
    }

    @Test
    fun `Given valid entity, when available roast types requested, then expected string is returned`(roastTypePermutations: RoastTypePermutations) {
        // When
        val result = roastTypePermutations.entity.availableRoastTypes()

        // Then
        assertThat(result).isEqualTo(roastTypePermutations.expectedOutput)
    }

    enum class RoastTypePermutations(val entity: BeanInfoItem, val expectedOutput: String) {
        ALL(
                BeanInfoItem("Country", "Roaster", hasSingleOrigin = true, hasBlendOrigin = true, hasLightRoast = true, hasMediumRoast = true, hasDarkRoast = true),
                "<b>Roast</b>: Light, Medium, Dark"
        ),
        ONLY_LIGHT(
                BeanInfoItem("Country", "Roaster", hasSingleOrigin = true, hasBlendOrigin = true, hasLightRoast = true, hasMediumRoast = false, hasDarkRoast = false),
                "<b>Roast</b>: Light"
        ),
        ONLY_DARK(
                BeanInfoItem("Country", "Roaster", hasSingleOrigin = true, hasBlendOrigin = true, hasLightRoast = false, hasMediumRoast = false, hasDarkRoast = true),
                "<b>Roast</b>: Dark"
        ),
        ONLY_MEDIUM(
                BeanInfoItem("Country", "Roaster", hasSingleOrigin = true, hasBlendOrigin = true, hasLightRoast = false, hasMediumRoast = true, hasDarkRoast = false),
                "<b>Roast</b>: Medium"
        ),
        MEDIUM_AND_DARK(
                BeanInfoItem("Country", "Roaster", hasSingleOrigin = true, hasBlendOrigin = true, hasLightRoast = false, hasMediumRoast = true, hasDarkRoast = true),
                "<b>Roast</b>: Medium, Dark"
        ),
        LIGHT_AND_MEDIUM(
                BeanInfoItem("Country", "Roaster", hasSingleOrigin = true, hasBlendOrigin = true, hasLightRoast = true, hasMediumRoast = true, hasDarkRoast = false),
                "<b>Roast</b>: Light, Medium"
        ),
        LIGHT_AND_DARK(
                BeanInfoItem("Country", "Roaster", hasSingleOrigin = true, hasBlendOrigin = true, hasLightRoast = true, hasMediumRoast = false, hasDarkRoast = true),
                "<b>Roast</b>: Light, Dark"
        ),
        NONE(
                BeanInfoItem("Country", "Roaster", hasSingleOrigin = true, hasBlendOrigin = true, hasLightRoast = false, hasMediumRoast = false, hasDarkRoast = false),
                "<b>Roast</b>: Information not available"
        )
    }
}