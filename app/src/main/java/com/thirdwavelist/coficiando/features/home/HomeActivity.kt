package com.thirdwavelist.coficiando.features.home

import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.thirdwavelist.coficiando.HomeActivityBinding
import com.thirdwavelist.coficiando.R
import com.thirdwavelist.coficiando.features.details.DetailsActivity
import com.thirdwavelist.coficiando.storage.repository.cafe.CafeRepository
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import android.databinding.adapters.TextViewBindingAdapter.setText
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.SearchView
import android.widget.ImageButton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class HomeActivity : DaggerAppCompatActivity() {

    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var binding: HomeActivityBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle
    @Inject lateinit var cafeRepository: CafeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        addDrawerItems();
        setupDrawer();

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setHomeButtonEnabled(true);
        supportActionBar?.title = "Third Wave List"

        viewModel = HomeActivityViewModel(cafeRepository, CafeAdapter(arrayListOf()))
        binding.viewModel = viewModel

        binding.recycler.layoutManager = LinearLayoutManager(this@HomeActivity)
        viewModel.adapter.setItemClickListener { position ->
            viewModel.adapter.getItem(position).let {
                startActivity(DetailsActivity.getStartIntent(
                    this@HomeActivity,
                    it.id
                ))
            }
        }

        viewModel.loadCafes()
    }

    override fun onStop() {
        viewModel.dispose()
        super.onStop()
    }

    private fun addDrawerItems() {
        val osArray = arrayOf("Android", "iOS", "Windows", "OS X", "Linux")
        binding.drawerList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, osArray)

        binding.drawerList.setOnItemClickListener({ _, _, _, _ -> Toast.makeText(this@HomeActivity, "Time for an upgrade!", Toast.LENGTH_SHORT).show() })
    }

    private fun setupDrawer() {
        drawerToggle = object : ActionBarDrawerToggle(this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely open state.  */
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                invalidateOptionsMenu() // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state.  */
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                invalidateOptionsMenu() // creates call to onPrepareOptionsMenu()
            }
        }

        drawerToggle.isDrawerIndicatorEnabled = true
        binding.drawerLayout.addDrawerListener(drawerToggle)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        menu.findItem(R.id.action_search).let {
            viewModel.enableSearch(it.actionView as SearchView)

//            (it.actionView.findViewById(android.support.v7.appcompat.R.id.search_close_btn) as AppCompatImageView)
//                .setOnClickListener {
//                    viewModel.loadCafes()
//                }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Activate the navigation drawer toggle
        return if (drawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)

    }

}