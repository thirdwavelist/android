package com.thirdwavelist.coficiando.features.details

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.thirdwavelist.coficiando.DetailsActivityBinding
import com.thirdwavelist.coficiando.R
import com.thirdwavelist.coficiando.storage.repository.cafe.CafeRepository
import dagger.android.support.DaggerAppCompatActivity
import java.util.UUID
import javax.inject.Inject

class DetailsActivity: DaggerAppCompatActivity() {

    private lateinit var viewModel: DetailsActivityViewModel
    private lateinit var binding: DetailsActivityBinding
    @Inject lateinit var cafeRepository: CafeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        viewModel = DetailsActivityViewModel(cafeRepository)
        binding.viewModel = viewModel

        viewModel.loadCafe(intent.cafeId)
    }

    companion object {
        private const val CAFE_ID = "com.thirdwavelist.coficiando.EXTRA_CAFE_ID"

        fun getStartIntent(context: Context, cafeId: UUID): Intent =
            Intent(context, DetailsActivity::class.java)
                .putExtra(CAFE_ID, cafeId)

        private val Intent.cafeId
            get() = getSerializableExtra(CAFE_ID) as UUID
    }
}