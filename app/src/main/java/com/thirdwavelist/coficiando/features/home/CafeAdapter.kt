/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2017, Antal János Monori
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.thirdwavelist.coficiando.features.home

import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.net.Uri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.thirdwavelist.coficiando.BR
import com.thirdwavelist.coficiando.CafeItemBinding
import com.thirdwavelist.coficiando.R
import com.thirdwavelist.coficiando.storage.db.cafe.BeanOriginType
import com.thirdwavelist.coficiando.storage.db.cafe.BeanRoastType
import com.thirdwavelist.coficiando.storage.db.cafe.CafeItem
import com.thirdwavelist.coficiando.storage.sharedprefs.FilterPrefsManager
import com.thirdwavelist.coficiando.storage.sharedprefs.UserPrefsManager


class CafeAdapter(private val filterPrefs: FilterPrefsManager,
                  private val userPrefs: UserPrefsManager)
    : RecyclerView.Adapter<CafeAdapter.CafeItemViewHolder>(), Filterable {

    private var itemClickListener: (position: Int) -> Unit = { _ -> run {} }

    var initialData: MutableList<CafeItem>? = null
        set(value) {
            if (field == null && value != null && value.isNotEmpty()) {
                field = value
            }
        }

    var data = listOf<CafeItem>()
        set(value) {
            // FIXME: Move the diffUtil calculations to the background thread with RxJava
            var newData = value
            val currItems = data
            if (userPrefs.isFilteringEnabled) {
                newData = newData
                    .filter {
                        (it.brewInfo.hasEspresso == filterPrefs.isInterestedInBrewMethodEspresso ||
                            it.brewInfo.hasAeropress == filterPrefs.isInterestedInBrewMethodAeropress ||
                            it.brewInfo.hasPourOver == filterPrefs.isInterestedInBrewMethodPourOver ||
                            it.brewInfo.hasColdBrew == filterPrefs.isInterestedInBrewMethodColdBrew ||
                            it.brewInfo.hasSyphon == filterPrefs.isInterestedInBrewMethodSyphon ||
                            it.brewInfo.hasFullImmersive == filterPrefs.isInterestedInBrewMethodFullImmersive) &&
                            (it.beanInfo.hasSingleOrigin == (filterPrefs.beanOriginType == BeanOriginType.SINGLE) &&
                                it.beanInfo.hasBlendOrigin == (filterPrefs.beanOriginType == BeanOriginType.BLEND) &&
                                (it.beanInfo.hasLightRoast == (filterPrefs.beanRoastType == BeanRoastType.LIGHT) &&
                                    it.beanInfo.hasMediumRoast == (filterPrefs.beanRoastType == BeanRoastType.MEDIUM) &&
                                    it.beanInfo.hasDarkRoast == (filterPrefs.beanRoastType == BeanRoastType.DARK)))
                    }
            }

            val diffResults = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize() = currItems.size

                override fun getNewListSize() = newData.size

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    currItems[oldItemPosition] == newData[newItemPosition]

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    currItems[oldItemPosition].id == newData[newItemPosition].id
            })
            field = newData
            diffResults.dispatchUpdatesTo(this@CafeAdapter)

        }

    override fun onBindViewHolder(holder: CafeItemViewHolder, position: Int) {
        data[position].let {
            holder.bind(CafeItemViewModel(title = it.name,
                thumbnailUri = it.thumbnail,
                hasEspresso = it.brewInfo.hasEspresso,
                hasAeropress = it.brewInfo.hasAeropress,
                hasColdBrew = it.brewInfo.hasColdBrew,
                hasPourOver = it.brewInfo.hasPourOver,
                hasSyphon = it.brewInfo.hasSyphon,
                hasImmersive = it.brewInfo.hasFullImmersive))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: CafeItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_cafe, parent, false)
        return CafeItemViewHolder(binding).also { it.setItemClickListener(itemClickListener) }
    }

    override fun getItemCount() = data.size

    override fun getFilter() = object : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            val results = mutableListOf<CafeItem>()
            val searchQuery = charSequence?.toString() ?: ""
            if (searchQuery.isNotEmpty()) {
                initialData?.forEach {
                    if (it.name.contains(searchQuery, ignoreCase = true)) results.add(it)
                }
            }

            return FilterResults().also {
                it.values = results
                it.count = results.size
            }
        }

        override fun publishResults(charSequence: CharSequence?, results: FilterResults?) {
            if (results != null) {
                data = results.values as List<CafeItem>
            }
        }
    }

    fun setItemClickListener(itemClickListener: (position: Int) -> Unit) {
        this.itemClickListener = itemClickListener
    }

    fun getItem(position: Int) = data[position]

    class CafeItemViewModel(title: String,
                            thumbnailUri: Uri,
                            hasEspresso: Boolean,
                            hasAeropress: Boolean,
                            hasColdBrew: Boolean,
                            hasPourOver: Boolean,
                            hasSyphon: Boolean,
                            hasImmersive: Boolean) {

        val title = ObservableField<String>(title)
        val thumbnail = ObservableField<Uri>(thumbnailUri)
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
            }
            binding.executePendingBindings()
        }

        fun setItemClickListener(itemClickListener: (position: Int) -> Unit) {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    itemClickListener(adapterPosition)
                }
            }
        }
    }

    fun resetData() {
        data = initialData ?: listOf()
    }
}