package com.thirdwavelist.coficiando.features.home

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.net.Uri
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.thirdwavelist.coficiando.BR
import com.thirdwavelist.coficiando.CafeItemBinding
import com.thirdwavelist.coficiando.R
import com.thirdwavelist.coficiando.storage.db.cafe.CafeItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class CafeAdapter(val data: List<CafeItem>) : RecyclerView.Adapter<CafeAdapter.CafeItemViewHolder>() {
    override fun onBindViewHolder(holder: CafeItemViewHolder, position: Int) {
        data[position].let {
            holder.bind(CafeItemViewModel(it.thumbnail))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: CafeItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_cafe, parent, false)

        return CafeItemViewHolder(binding)
    }

    override fun getItemCount() = data.size

    fun setItems(newData: List<CafeItem>) {
        Observable.fromCallable {
            val currItems = data.toMutableList()

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
                data.update {
                    this@update.clear()
                    this@update.addAll(newData)
                }
                it.dispatchUpdatesTo(this@CafeAdapter)
            }
    }


    class CafeItemViewModel(thumbnailUri: Uri) {
        val thumbnail = ObservableField<Uri>(thumbnailUri)
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

inline fun <T> List<T>.update(mutatorBlock: MutableList<T>.() -> Unit): List<T> {
    return toMutableList().apply(mutatorBlock)
}
