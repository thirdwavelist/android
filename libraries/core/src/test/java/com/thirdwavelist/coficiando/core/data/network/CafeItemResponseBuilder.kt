package com.thirdwavelist.coficiando.core.data.network

import com.thirdwavelist.coficiando.coroutines.data.network.model.CafeItemDto
import com.thirdwavelist.coficiando.coroutines.data.network.model.ErrorResponseDto
import java.util.UUID

internal object CafeItemResponseBuilder {
    /**
     * Use in conjunction with the output from getOne_success.json
     */
    fun single(): CafeItemDto = KAFFEE_FABRIK

    /**
     * Use in conjunction with the output from getAll_success.json
     */
    fun multiple(): List<CafeItemDto> = listOf(
            ONE_CUP, KAFFEE_FABRIK,
    )

    /**
     * Use in conjunction with the output from getOne_error.json or getAll_error.json
     */
    fun forbidden(): ErrorResponseDto = ErrorResponseDto("Forbidden")

    private val ONE_CUP = CafeItemDto(
            id = UUID.fromString("4e0cea4f-5b69-4a1d-9aec-d3323fbbfac6"),
            name = "OneCup",
            address = "Baross u. 1, 1082 Hungary",
            thumbnail = "https://assets.thirdwavelist.com/thumb/4e0cea4f-5b69-4a1d-9aec-d3323fbbfac6.jpg",
            socialWebsite = " ",
            socialFacebook = "https://www.facebook.com/OneCupEspressoBar/",
            socialInstagram = "https://www.instagram.com/onecup_espresso_bar/",
            doesServeEspresso = true,
            doesServeColdBrew = true,
            doesServePourOver = true,
            doesServeSyphon = false,
            doesServeAeropress = true,
            doesServeFullImmersive = true,
            gearEspressoMachine = "Synesso",
            gearGrinder = "Compak",
            gearPourOver = "V60",
            gearFullImmersive = "French press",
            beanRoaster = "Lucky Cap \uD83C\uDDED\uD83C\uDDFA",
            beanOrigin = " ",
            beanOriginSingle = true,
            beanOriginBlend = false,
            beanRoastLight = true,
            beanRoastMedium = true,
            beanRoastDark = false,
            priceDoppio = "Ft0.00",
            googlePlaceId = "ChIJ3VrJAVvcQUcRUi3b2SKkhPo"
    )

    private val KAFFEE_FABRIK = CafeItemDto(
            id = UUID.fromString("5a5b4020-315e-4504-9068-a6381dd45383"),
            name = "kaffeefabrik",
            address = "Favoritenstraße 4-6, 1040 Wien, Austria",
            thumbnail = " ",
            socialWebsite = "http://www.kaffeefabrik.at/",
            socialFacebook = "https://www.facebook.com/kaffeefabrik/",
            socialInstagram = " ",
            doesServeEspresso = true,
            doesServeColdBrew = true,
            doesServePourOver = true,
            doesServeSyphon = false,
            doesServeAeropress = true,
            doesServeFullImmersive = false,
            gearEspressoMachine = "Dalla Corte Evo2 - 2 group",
            gearGrinder = "Fiorenzato F64, Mahlkönig EK43 Special Edition",
            gearPourOver = "V60",
            gearFullImmersive = " ",
            beanRoaster = "Kaffeefabrik \uD83C\uDDE6\uD83C\uDDF9",
            beanOrigin = "Ethiopia (single filter, espresso blend with a second Ethiopian), Brazil (single Espresso, espresso blend, soon single filter again), sumatra (single Espresso), India (Robusta in Blends), Guatemala (in Espresso Blend and single Filter) Honduras (in Espresso Blend and single filter), Peru (in Espresso Blend), Nicaragua (single Espresso), Ecuador (single Espresso)",
            beanOriginSingle = true,
            beanOriginBlend = true,
            beanRoastLight = true,
            beanRoastMedium = true,
            beanRoastDark = true,
            priceDoppio = " ",
            googlePlaceId = "ChIJ0Qu4L4IHbUcRfDplVg1DT3Q"
    )
}