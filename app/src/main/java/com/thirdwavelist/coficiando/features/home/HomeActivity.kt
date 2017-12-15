package com.thirdwavelist.coficiando.features.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.thirdwavelist.coficiando.HomeActivityBinding
import com.thirdwavelist.coficiando.R
import com.thirdwavelist.coficiando.storage.repository.cafe.CafeRepository
import dagger.android.DaggerActivity
import javax.inject.Inject

class HomeActivity : DaggerActivity() {

    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var binding: HomeActivityBinding
    @Inject lateinit var cafeRepository: CafeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        viewModel = HomeActivityViewModel(cafeRepository, CafeAdapter(arrayListOf()))
        binding.viewModel = viewModel

        binding.recycler.layoutManager = LinearLayoutManager(this@HomeActivity)

        viewModel.getCafes()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onStop() {
        viewModel.dispose()
        super.onStop()
    }
}