package com.thirdwavelist.coficiando.storage.db.cafe

data class BeanInfoItem(val origin: String?,
                        val roaster: String?,
                        val hasSingleOrigin: Boolean,
                        val hasBlendOrigin: Boolean,
                        val hasLightRoast: Boolean,
                        val hasMediumRoast: Boolean,
                        val hasDarkRoast: Boolean)

fun BeanInfoItem.availableRoastTypes(): String {
    val results = arrayListOf<String>()
    if (hasLightRoast) results.add("Light")
    if (hasMediumRoast) results.add("Medium")
    if (hasDarkRoast) results.add("Dark")

    return "<b>Roast</b>: "+results.joinToString(separator = ", ")
}

fun BeanInfoItem.availableOriginTypes(): String {
    val results = arrayListOf<String>()
    if (hasSingleOrigin) results.add("Single")
    if (hasBlendOrigin) results.add("Blend")
    return "<b>Origin</b>: "+results.joinToString(separator = ", ")
}