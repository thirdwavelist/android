package com.thirdwavelist.coficiando.features.home

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.thirdwavelist.coficiando.HomeActivityBinding
import com.thirdwavelist.coficiando.R
import com.thirdwavelist.coficiando.features.shared.ViewModelFactory
import com.thirdwavelist.coficiando.storage.repository.cafe.CafeRepository
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity() {

    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var binding: HomeActivityBinding
    private lateinit var viewModelFactory: ViewModelFactory
    @Inject lateinit var cafeRepository: CafeRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewModelFactory = ViewModelFactory(cafeRepository)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeActivityViewModel::class.java)
        binding.viewModel = viewModel
    }
}