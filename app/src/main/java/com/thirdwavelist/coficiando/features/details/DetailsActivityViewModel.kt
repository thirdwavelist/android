package com.thirdwavelist.coficiando.features.details

import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.view.View
import com.thirdwavelist.coficiando.storage.db.cafe.CafeItem
import com.thirdwavelist.coficiando.storage.db.cafe.availableOriginTypes
import com.thirdwavelist.coficiando.storage.db.cafe.availableRoastTypes
import com.thirdwavelist.coficiando.storage.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.UUID

class DetailsActivityViewModel(private val repository: Repository<CafeItem>) : ViewModel() {
    val name = ObservableField<String>("")
    val thumbnail = ObservableField<Uri>(Uri.EMPTY)
    val googlePlaceId = ObservableField<String>("")
    val espressoMachineName = ObservableField<String>("")
    val grinderMachineName = ObservableField<String>("")
    val availableBeanOrigin = ObservableField<String>("")
    val availableBeanRoast = ObservableField<String>("")
    val hasEspresso = ObservableBoolean()
    val hasAeropress = ObservableBoolean()
    val hasColdBrew = ObservableBoolean()
    val hasPourOver = ObservableBoolean()
    val hasSyphon = ObservableBoolean()
    val hasImmersive = ObservableBoolean()
    val isFinishedLoading = ObservableBoolean()
    val hasFacebook = ObservableBoolean()
    val facebookUri = ObservableField<Uri>(Uri.EMPTY)
    val hasInstagram = ObservableBoolean()
    val instagramUri = ObservableField<Uri>(Uri.EMPTY)
    val hasWebsite = ObservableBoolean()
    val websiteUri = ObservableField<Uri>(Uri.EMPTY)
    val isError = ObservableBoolean()

    fun mapAction(view: View)  {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            addCategory(Intent.CATEGORY_BROWSABLE)
            data = Uri.parse("https://www.google.com/maps/search/?api=1&query=0,0&query_place_id=${googlePlaceId.get()}")
        }
        startActivity(view.context, intent, null)
    }

    fun navigateToFacebook(view: View)  {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            addCategory(Intent.CATEGORY_BROWSABLE)
            data = facebookUri.get()
        }
        startActivity(view.context, intent, null)
    }

    fun navigateToInstagram(view: View)  {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            addCategory(Intent.CATEGORY_BROWSABLE)
            data = instagramUri.get()
        }
        startActivity(view.context, intent, null)
    }

    fun navigateToWebsite(view: View)  {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            addCategory(Intent.CATEGORY_BROWSABLE)
            data = websiteUri.get()
        }
        startActivity(view.context, intent, null)
    }

    private val disposables = CompositeDisposable()

    private operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
        add(disposable)
    }

    fun loadCafe(cafeId: UUID) {
        disposables += repository
            .get(cafeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    /* onSuccess */
                    handleResponse(it)
                    isFinishedLoading.set(true)
                    isError.set(false)
                },
                {
                    /* onError */
                    handleError()
                    isFinishedLoading.set(true)
                    isError.set(true)
                }
            )
    }

    private fun handleError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun handleResponse(it: CafeItem) {
        name.set(it.name)
        thumbnail.set(it.thumbnail)
        googlePlaceId.set(it.googlePlaceId)
        espressoMachineName.set(it.gearInfo.espressoMachineName)
        grinderMachineName.set(it.gearInfo.grinderMachineName)
        availableBeanOrigin.set(it.beanInfo.availableOriginTypes())
        availableBeanRoast.set(it.beanInfo.availableRoastTypes())
        hasFacebook.set(it.social.facebookUri != Uri.EMPTY)
        facebookUri.set(it.social.facebookUri ?: Uri.EMPTY)
        hasInstagram.set(it.social.instagramUri != Uri.EMPTY)
        instagramUri.set(it.social.instagramUri ?: Uri.EMPTY)
        hasWebsite.set(it.social.homepageUri != Uri.EMPTY)
        websiteUri.set(it.social.homepageUri ?: Uri.EMPTY)
        hasEspresso.set(it.brewInfo.hasEspresso)
        hasAeropress.set(it.brewInfo.hasAeropress)
        hasColdBrew.set(it.brewInfo.hasColdBrew)
        hasSyphon.set(it.brewInfo.hasSyphon)
        hasPourOver.set(it.brewInfo.hasPourOver)
        hasImmersive.set(it.brewInfo.hasFullImmersive)
    }
}