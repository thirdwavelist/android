package com.thirdwavelist.coficiando.details.presentation

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.thirdwavelist.coficiando.core.domain.cafe.BeanInfoItem.Companion.availableOriginTypes
import com.thirdwavelist.coficiando.core.domain.cafe.BeanInfoItem.Companion.availableRoastTypes
import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem
import com.thirdwavelist.coficiando.details.domain.GetCafeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
internal class DetailsFragmentViewModel @Inject constructor(
        private val getCafeUseCase: GetCafeUseCase
) : ViewModel() {

    val name = ObservableField("")
    val thumbnail = ObservableField(Uri.EMPTY)
    private val googlePlaceId = ObservableField("")
    val espressoMachineName = ObservableField("")
    val grinderMachineName = ObservableField("")
    val availableBeanOrigin = ObservableField("")
    val availableBeanRoast = ObservableField("")
    val hasEspresso = ObservableBoolean()
    val hasAeropress = ObservableBoolean()
    val hasColdBrew = ObservableBoolean()
    val hasPourOver = ObservableBoolean()
    val hasSyphon = ObservableBoolean()
    val hasImmersive = ObservableBoolean()
    val isFinishedLoading = ObservableBoolean()
    val hasFacebook = ObservableBoolean()
    private val facebookUri = ObservableField(Uri.EMPTY)
    val hasInstagram = ObservableBoolean()
    private val instagramUri = ObservableField(Uri.EMPTY)
    val hasWebsite = ObservableBoolean()
    private val websiteUri = ObservableField(Uri.EMPTY)

    fun mapAction(view: View) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            addCategory(Intent.CATEGORY_BROWSABLE)
            data = Uri.parse("https://www.google.com/maps/search/?api=1&query=0,0&query_place_id=${googlePlaceId.get()}")
        }
        startActivity(view.context, intent, null)
    }

    fun navigateToFacebook(view: View) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            addCategory(Intent.CATEGORY_BROWSABLE)
            data = facebookUri.get()
        }
        startActivity(view.context, intent, null)
    }

    fun navigateToInstagram(view: View) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            addCategory(Intent.CATEGORY_BROWSABLE)
            data = instagramUri.get()
        }
        startActivity(view.context, intent, null)
    }

    fun navigateToWebsite(view: View) {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            addCategory(Intent.CATEGORY_BROWSABLE)
            data = websiteUri.get()
        }
        startActivity(view.context, intent, null)
    }

    fun loadCafe(cafeId: UUID) {
        getCafeUseCase.withParams(cafeId).execute {
            onComplete {
                handleResponse(it)
            }
            onError {
                handleError()
            }
        }
    }

    private fun handleError() {
        TODO("Not yet implemented")
    }

    private fun handleResponse(it: CafeItem?) {
        if (it == null) return
        name.set(it.name)
        thumbnail.set(Uri.parse(it.thumbnail))
        googlePlaceId.set(it.googlePlaceId)
        espressoMachineName.set(it.gearInfo.espressoMachineName)
        grinderMachineName.set(it.gearInfo.grinderMachineName)
        availableBeanOrigin.set(it.beanInfo.availableOriginTypes())
        availableBeanRoast.set(it.beanInfo.availableRoastTypes())
        hasFacebook.set(it.social.facebookUri.isNullOrBlank())
        facebookUri.set(if (it.social.facebookUri != null) Uri.parse(it.social.facebookUri) else Uri.EMPTY)
        hasInstagram.set(it.social.instagramUri.isNullOrBlank())
        instagramUri.set(if (it.social.instagramUri != null) Uri.parse(it.social.instagramUri) else Uri.EMPTY)
        hasWebsite.set(it.social.homepageUri.isNullOrBlank())
        websiteUri.set(if (it.social.homepageUri != null) Uri.parse(it.social.homepageUri) else Uri.EMPTY)
        hasEspresso.set(it.brewInfo.hasEspresso)
        hasAeropress.set(it.brewInfo.hasAeropress)
        hasColdBrew.set(it.brewInfo.hasColdBrew)
        hasSyphon.set(it.brewInfo.hasSyphon)
        hasPourOver.set(it.brewInfo.hasPourOver)
        hasImmersive.set(it.brewInfo.hasFullImmersive)
    }
}