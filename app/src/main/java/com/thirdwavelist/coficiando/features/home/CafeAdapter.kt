package com.thirdwavelist.coficiando.features.home

import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.thirdwavelist.coficiando.BR
import com.thirdwavelist.coficiando.CafeItemBinding
import com.thirdwavelist.coficiando.R
import com.thirdwavelist.coficiando.storage.db.cafe.CafeItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class CafeAdapter(initialData: List<CafeItem> = listOf()) : RecyclerView.Adapter<CafeAdapter.CafeItemViewHolder>(), Filterable {
    override fun getFilter() = object : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val results = mutableListOf<CafeItem>()
            val searchQuery = charSequence.toString()
            if (searchQuery.isNotEmpty()) {
                data.forEach {
                    if (it.name.contains(searchQuery, ignoreCase = true)) results.add(it)
                }
            }

            return FilterResults().also {
                it.values = results
                it.count = results.size
            }
        }

        override fun publishResults(charSequence: CharSequence, results: FilterResults) {
            data = results.values as List<CafeItem>
        }

    }

    var data = initialData
        set(newData) {
            Observable.fromCallable {
                val currItems = data

                DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                    override fun getOldListSize() = currItems.size
                    override fun getNewListSize() = newData.size
                    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = currItems[oldItemPosition] == newData[newItemPosition]
                    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = currItems[oldItemPosition].id == newData[newItemPosition].id
                })
            }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    field = newData
                    it.dispatchUpdatesTo(this@CafeAdapter)
                }
        }

    override fun onBindViewHolder(holder: CafeItemViewHolder, position: Int) {
        data[position].let {
            val socialAction: () -> Unit = {
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    addCategory(Intent.CATEGORY_BROWSABLE)
                    data = it.social.facebookUri
                }
                startActivity(holder.itemView.context, intent, null)
            }

            val navigateToAction: () -> Unit = {
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    addCategory(Intent.CATEGORY_BROWSABLE)
                    data = Uri.parse("https://www.google.com/maps/search/?api=1&query=0,0&query_place_id=${it.googlePlaceId}")
                }
                startActivity(holder.itemView.context, intent, null)
            }

            holder.bind(CafeItemViewModel(it.name, it.thumbnail, "Facebook", socialAction, navigateToAction))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: CafeItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_cafe, parent, false)
        return CafeItemViewHolder(binding)
    }

    override fun getItemCount() = data.size

    class CafeItemViewModel(title: String,
                            thumbnailUri: Uri,
                            socialText: String,
                            private val socialActionIntent: () -> Unit = {},
                            private val navigateToActionIntent: () -> Unit = {}) {

        val title = ObservableField<String>(title)
        val thumbnail = ObservableField<Uri>(thumbnailUri)
        val social = ObservableField<String>(socialText)

        @Suppress("UNUSED_PARAMETER")
        fun socialAction(view: View) = socialActionIntent.invoke()
        @Suppress("UNUSED_PARAMETER")
        fun navigateToAction(view: View) = navigateToActionIntent.invoke()
    }

    class CafeItemViewHolder(private val binding: CafeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: Any) {
            when (viewModel) {
                is CafeItemViewModel -> binding.setVariable(BR.viewModel, viewModel)
            }
            binding.executePendingBindings()
        }
    }
}