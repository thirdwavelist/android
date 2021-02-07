package com.thirdwavelist.coficiando.details.presentation

sealed class DetailsViewState {
    object Loading : DetailsViewState()
    object Error : DetailsViewState()
    data class Success(
            val name: String,
            val thumbnailUrl: String,
            val googleMapsUri: String,
            val espressoMachineName: String?,
            val grinderMachineName: String?,
            val availableBeanOrigin: String?,
            val availableBeanRoast: String?,
            val hasEspresso: Boolean,
            val hasAeropress: Boolean,
            val hasColdBrew: Boolean,
            val hasPourOver: Boolean,
            val hasSyphon: Boolean,
            val hasImmersive: Boolean,
            val facebookUri: String?,
            val instagramUri: String?,
            val websiteUri: String?,
    ) : DetailsViewState()
}