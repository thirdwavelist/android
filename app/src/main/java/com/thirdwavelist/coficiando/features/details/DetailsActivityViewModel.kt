package com.thirdwavelist.coficiando.features.details

import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.view.View
import com.thirdwavelist.coficiando.storage.db.cafe.CafeItem
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
    val beanOrigin = ObservableField<String>("")
    val isFinishedLoading = ObservableBoolean()
    val isError = ObservableBoolean()

    fun mapAction(view: View)  {
        val intent = Intent().apply {
            action = Intent.ACTION_VIEW
            addCategory(Intent.CATEGORY_BROWSABLE)
            data = Uri.parse("https://www.google.com/maps/search/?api=1&query=0,0&query_place_id=${googlePlaceId.get()}")
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
        beanOrigin.set(it.beanInfo.origin)
    }
}