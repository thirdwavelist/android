package com.thirdwavelist.coficiando.home.presentation

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thirdwavelist.coficiando.core.domain.cafe.CafeItem
import com.thirdwavelist.coficiando.core.glide.setImage
import com.thirdwavelist.coficiando.home.databinding.ItemCafeBinding

private val diffUtil = object : DiffUtil.ItemCallback<CafeItem>() {
    override fun areItemsTheSame(oldItem: CafeItem, newItem: CafeItem): Boolean {
        return oldItem.id == newItem.id
                && oldItem.name == newItem.name
                && oldItem.thumbnail == newItem.thumbnail
    }

    override fun areContentsTheSame(oldItem: CafeItem, newItem: CafeItem) = oldItem == newItem
}

class CafeAdapter : ListAdapter<CafeItem, CafeAdapter.CafeItemViewHolder>(diffUtil) {

    private lateinit var itemClickListener: (CafeItem, List<Pair<View, String>>) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemCafeBinding = ItemCafeBinding.inflate(layoutInflater, parent, false)
        return CafeItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CafeItemViewHolder, position: Int) {
        getItem(position).let {
            val viewModel = CafeItemViewModel(title = it.name,
                    thumbnailUrl = it.thumbnail,
                    hasEspresso = it.brewInfo.hasEspresso,
                    hasAeropress = it.brewInfo.hasAeropress,
                    hasColdBrew = it.brewInfo.hasColdBrew,
                    hasPourOver = it.brewInfo.hasPourOver,
                    hasSyphon = it.brewInfo.hasSyphon,
                    hasImmersive = it.brewInfo.hasFullImmersive)
            holder.bind(viewModel)
            holder.setItemClickListener(itemClickListener, it)
        }
    }

    fun setOnItemClickListener(function: (CafeItem, List<Pair<View, String>>) -> Unit) {
        itemClickListener = function
    }

    data class CafeItemViewModel(val title: String,
                                 val thumbnailUrl: String,
                                 val hasEspresso: Boolean,
                                 val hasAeropress: Boolean,
                                 val hasColdBrew: Boolean,
                                 val hasPourOver: Boolean,
                                 val hasSyphon: Boolean,
                                 val hasImmersive: Boolean)

    class CafeItemViewHolder(private val binding: ItemCafeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: Any) {
            when (viewModel) {
                is CafeItemViewModel -> {
                    if (viewModel.thumbnailUrl.isNotBlank()) {
                        setImage(binding.thumbnail, Uri.parse(viewModel.thumbnailUrl))
                    }
                    binding.title.text = viewModel.title
                    binding.hasEspresso.isVisible = viewModel.hasEspresso
                    binding.hasAeropress.isVisible = viewModel.hasAeropress
                    binding.hasColdBrew.isVisible = viewModel.hasColdBrew
                    binding.hasPourOver.isVisible = viewModel.hasPourOver
                    binding.hasSyphon.isVisible = viewModel.hasSyphon
                    binding.hasImmersive.isVisible = viewModel.hasImmersive
                }
                else -> throw IllegalArgumentException("Unknown viewModel type: ${viewModel::class.java.name}")
            }
        }

        fun setItemClickListener(itemClickListener: (CafeItem, List<Pair<View, String>>) -> Unit, cafeItem: CafeItem) {
            val sharedElementTransitions = arrayOf(
                    binding.thumbnail as View to "detailsThumbnail"
            )
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    itemClickListener(cafeItem, sharedElementTransitions.toList())
                }
            }

        }
    }
}