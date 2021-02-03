package com.thirdwavelist.coficiando.home

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thirdwavelist.coficiando.core.storage.db.cafe.CafeItem

private val diffUtil = object : DiffUtil.ItemCallback<CafeItem>() {
    override fun areItemsTheSame(oldItem: CafeItem, newItem: CafeItem): Boolean {
        return oldItem.id == newItem.id
                && oldItem.name == newItem.name
                && oldItem.thumbnail == newItem.thumbnail
    }

    override fun areContentsTheSame(oldItem: CafeItem, newItem: CafeItem) = oldItem == newItem
}

class CafeAdapter(private val onItemClickListener: OnItemSelectedListener) : ListAdapter<CafeItem, CafeAdapter.CafeItemViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: CafeItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_cafe, parent, false)
        return CafeItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CafeItemViewHolder, position: Int) {
        getItem(position).let {
            val viewModel = CafeItemViewModel(title = it.name,
                    thumbnailUri = it.thumbnail,
                    hasEspresso = it.brewInfo.hasEspresso,
                    hasAeropress = it.brewInfo.hasAeropress,
                    hasColdBrew = it.brewInfo.hasColdBrew,
                    hasPourOver = it.brewInfo.hasPourOver,
                    hasSyphon = it.brewInfo.hasSyphon,
                    hasImmersive = it.brewInfo.hasFullImmersive)
            holder.bind(viewModel)
            holder.setItemClickListener(onItemClickListener, it)
        }
    }

    class CafeItemViewModel(title: String,
                            thumbnailUri: Uri,
                            hasEspresso: Boolean,
                            hasAeropress: Boolean,
                            hasColdBrew: Boolean,
                            hasPourOver: Boolean,
                            hasSyphon: Boolean,
                            hasImmersive: Boolean) {

        val title = ObservableField(title)
        val thumbnail = ObservableField(thumbnailUri)
        val hasEspresso = ObservableBoolean(hasEspresso)
        val hasAeropress = ObservableBoolean(hasAeropress)
        val hasColdBrew = ObservableBoolean(hasColdBrew)
        val hasPourOver = ObservableBoolean(hasPourOver)
        val hasSyphon = ObservableBoolean(hasSyphon)
        val hasImmersive = ObservableBoolean(hasImmersive)
    }

    class CafeItemViewHolder(private val binding: CafeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: Any) {
            when (viewModel) {
                is CafeItemViewModel -> binding.setVariable(BR.viewModel, viewModel)
                else -> throw IllegalArgumentException("Unknown viewModel type: ${viewModel::class.java.name}")
            }
            binding.executePendingBindings()
        }

        fun setItemClickListener(itemClickListener: OnItemSelectedListener, cafeItem: CafeItem) {
            val sharedElementTransitions = arrayOf(
                    binding.thumbnail as View to "detailsThumbnail"
            )
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemSelected(cafeItem, sharedElementTransitions)
                }
            }

        }
    }

    interface OnItemSelectedListener {
        fun onItemSelected(item: CafeItem, sharedElementTransitions: Array<Pair<View, String>>)
    }
}