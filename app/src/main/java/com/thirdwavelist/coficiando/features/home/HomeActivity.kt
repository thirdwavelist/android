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

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import com.thirdwavelist.coficiando.HomeActivityBinding
import com.thirdwavelist.coficiando.R
import com.thirdwavelist.coficiando.features.details.DetailsActivity
import com.thirdwavelist.coficiando.storage.db.cafe.BeanOriginType
import com.thirdwavelist.coficiando.storage.db.cafe.BeanRoastType
import com.thirdwavelist.coficiando.storage.repository.cafe.CafeRepository
import com.thirdwavelist.coficiando.storage.sharedprefs.FilterPrefsManager
import com.thirdwavelist.coficiando.storage.sharedprefs.UserPrefsManager
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class HomeActivity : DaggerAppCompatActivity() {

    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var binding: HomeActivityBinding
    private lateinit var drawerToggle: ActionBarDrawerToggle
    @Inject lateinit var cafeRepository: CafeRepository
    @Inject lateinit var filterPrefs: FilterPrefsManager
    @Inject lateinit var userPrefs: UserPrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewModel = HomeActivityViewModel(cafeRepository, CafeAdapter(filterPrefs, userPrefs))
        binding.viewModel = viewModel

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        setupDrawer()

        binding.recycler.layoutManager = getLayoutManager()
        viewModel.adapter.setItemClickListener { position ->
            viewModel.adapter.getItem(position).let {
                startActivity(DetailsActivity.getStartIntent(
                    this@HomeActivity,
                    it.id
                ))
            }
        }

        viewModel.loadCafes()

        handleIntent(intent)
    }

    private fun getLayoutManager(): RecyclerView.LayoutManager {
        val smallestWidth = resources.configuration.smallestScreenWidthDp

        if (smallestWidth >= 600) {
            return GridLayoutManager(this@HomeActivity, 2)
        } else {
            return if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                LinearLayoutManager(this@HomeActivity)
            } else {
                GridLayoutManager(this@HomeActivity, 2)
            }
        }
    }

    override fun onStop() {
        viewModel.dispose()
        super.onStop()
    }

    private fun setupDrawer() {
        drawerToggle = object : ActionBarDrawerToggle(this, binding.drawerLayout, R.string.alt_navigation_drawer_open, R.string.alt_navigation_drawer_close) {

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
        setupMenuItems()
    }

    private fun setupMenuItems() {
        val enableFiltersChangeListener = CompoundButton.OnCheckedChangeListener { checkBox, value ->
            when (checkBox?.id) {
                R.id.nav_enable_filters -> {
                    userPrefs.isFilteringEnabled = value
                    updateMenuItemState(value)
                }
            }
        }
        val filterModeChangeListener = CompoundButton.OnCheckedChangeListener { checkBox, value ->
            updateFilterPreference(checkBox?.id, value)
        }

        ((binding.drawerList.menu.findItem(R.id.nav_enable_filters)).actionView as Switch).let {
            it.isChecked = false
            userPrefs.isFilteringEnabled = false
            it.setOnCheckedChangeListener(enableFiltersChangeListener)
        }

        (binding.drawerList.menu.findItem(R.id.brew_method_espresso).actionView as CompoundButton).let {
            it.isChecked = filterPrefs.isInterestedInBrewMethodEspresso
            it.setOnCheckedChangeListener(filterModeChangeListener)
        }

        (binding.drawerList.menu.findItem(R.id.brew_method_aeropress).actionView as CompoundButton).let {
            it.isChecked = filterPrefs.isInterestedInBrewMethodAeropress
            it.setOnCheckedChangeListener(filterModeChangeListener)
        }
        (binding.drawerList.menu.findItem(R.id.brew_method_cold_brew).actionView as CompoundButton).let {
            it.isChecked = filterPrefs.isInterestedInBrewMethodColdBrew
            it.setOnCheckedChangeListener(filterModeChangeListener)
        }
        (binding.drawerList.menu.findItem(R.id.brew_method_pour_over).actionView as CompoundButton).let {
            it.isChecked = filterPrefs.isInterestedInBrewMethodPourOver
            it.setOnCheckedChangeListener(filterModeChangeListener)
        }
        (binding.drawerList.menu.findItem(R.id.brew_method_syphon).actionView as CompoundButton).let {
            it.isChecked = filterPrefs.isInterestedInBrewMethodSyphon
            it.setOnCheckedChangeListener(filterModeChangeListener)
        }
        (binding.drawerList.menu.findItem(R.id.brew_method_full_immersion).actionView as CompoundButton).let {
            it.isChecked = filterPrefs.isInterestedInBrewMethodFullImmersive
            it.setOnCheckedChangeListener(filterModeChangeListener)
        }
        (binding.drawerList.menu.findItem(R.id.bean_origin_single).actionView as CompoundButton).let {
            it.isChecked = filterPrefs.beanOriginType == BeanOriginType.SINGLE
            it.setOnCheckedChangeListener({ checkBox, value ->
                if (value) {
                    updateFilterPreference(checkBox?.id, value)
                    (binding.drawerList.menu.findItem(R.id.bean_origin_blend).actionView as CompoundButton).isChecked = false
                }
            })
        }
        (binding.drawerList.menu.findItem(R.id.bean_origin_blend).actionView as CompoundButton).let {
            it.isChecked = filterPrefs.beanOriginType == BeanOriginType.BLEND
            it.setOnCheckedChangeListener({ checkBox, value ->
                if (value) {
                    updateFilterPreference(checkBox?.id, value)
                    (binding.drawerList.menu.findItem(R.id.bean_origin_single).actionView as CompoundButton).isChecked = false
                }
            })
        }
        (binding.drawerList.menu.findItem(R.id.bean_roast_light).actionView as CompoundButton).let {
            it.isChecked = filterPrefs.beanRoastType == BeanRoastType.LIGHT
            it.setOnCheckedChangeListener({ checkBox, value ->
                if (value) {
                    updateFilterPreference(checkBox?.id, value)
                    (binding.drawerList.menu.findItem(R.id.bean_roast_medium).actionView as CompoundButton).isChecked = false
                    (binding.drawerList.menu.findItem(R.id.bean_roast_dark).actionView as CompoundButton).isChecked = false
                }
            })
        }
        (binding.drawerList.menu.findItem(R.id.bean_roast_medium).actionView as CompoundButton).let {
            it.isChecked = filterPrefs.beanRoastType == BeanRoastType.MEDIUM
            it.setOnCheckedChangeListener({ checkBox, value ->
                if (value) {
                    updateFilterPreference(checkBox?.id, value)
                    (binding.drawerList.menu.findItem(R.id.bean_roast_dark).actionView as CompoundButton).isChecked = false
                    (binding.drawerList.menu.findItem(R.id.bean_roast_light).actionView as CompoundButton).isChecked = false
                }
            })
        }
        (binding.drawerList.menu.findItem(R.id.bean_roast_dark).actionView as CompoundButton).let {
            it.isChecked = filterPrefs.beanRoastType == BeanRoastType.DARK
            it.setOnCheckedChangeListener({ checkBox, value ->
                if (value) {
                    updateFilterPreference(checkBox?.id, value)
                    (binding.drawerList.menu.findItem(R.id.bean_roast_medium).actionView as CompoundButton).isChecked = false
                    (binding.drawerList.menu.findItem(R.id.bean_roast_light).actionView as CompoundButton).isChecked = false
                }
            })
        }

        updateMenuItemState(isEnabled = userPrefs.isFilteringEnabled)
    }

    private fun updateMenuItemState(isEnabled: Boolean) {
        enableMenuItems(binding.drawerList.menu.findItem(R.id.brew_method_espresso), isEnabled)
        enableMenuItems(binding.drawerList.menu.findItem(R.id.brew_method_aeropress), isEnabled)
        enableMenuItems(binding.drawerList.menu.findItem(R.id.brew_method_cold_brew), isEnabled)
        enableMenuItems(binding.drawerList.menu.findItem(R.id.brew_method_pour_over), isEnabled)
        enableMenuItems(binding.drawerList.menu.findItem(R.id.brew_method_syphon), isEnabled)
        enableMenuItems(binding.drawerList.menu.findItem(R.id.brew_method_full_immersion), isEnabled)
        enableMenuItems(binding.drawerList.menu.findItem(R.id.bean_origin_single), isEnabled)
        enableMenuItems(binding.drawerList.menu.findItem(R.id.bean_origin_blend), isEnabled)
        enableMenuItems(binding.drawerList.menu.findItem(R.id.bean_roast_light), isEnabled)
        enableMenuItems(binding.drawerList.menu.findItem(R.id.bean_roast_medium), isEnabled)
        enableMenuItems(binding.drawerList.menu.findItem(R.id.bean_roast_dark), isEnabled)
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
            (it.actionView as SearchView).let {
                it.setSearchableInfo((getSystemService(Context.SEARCH_SERVICE) as SearchManager).getSearchableInfo(componentName))
                (it.findViewById(R.id.search_close_btn) as AppCompatImageView).setOnClickListener {
                    viewModel.adapter.resetData()
                    invalidateOptionsMenu()
                }
            }

            it.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                    viewModel.adapter.resetData()
                    return true
                }

                override fun onMenuItemActionExpand(p0: MenuItem?) = true
            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Activate the navigation drawer toggle
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
        super.onNewIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            viewModel.adapter.filter.filter(query)
        }
    }

    private fun updateFilterPreference(id: Int?, value: Boolean) {
        when (id) {
            R.id.brew_method_espresso -> filterPrefs.isInterestedInBrewMethodEspresso = value
            R.id.brew_method_aeropress -> filterPrefs.isInterestedInBrewMethodAeropress = value
            R.id.brew_method_cold_brew -> filterPrefs.isInterestedInBrewMethodColdBrew = value
            R.id.brew_method_pour_over -> filterPrefs.isInterestedInBrewMethodPourOver = value
            R.id.brew_method_syphon -> filterPrefs.isInterestedInBrewMethodSyphon = value
            R.id.brew_method_full_immersion -> filterPrefs.isInterestedInBrewMethodFullImmersive = value
            R.id.bean_origin_single -> filterPrefs.beanOriginType = BeanOriginType.SINGLE
            R.id.bean_origin_blend -> filterPrefs.beanOriginType = BeanOriginType.BLEND
            R.id.bean_roast_light -> filterPrefs.beanRoastType = BeanRoastType.LIGHT
            R.id.bean_roast_medium -> filterPrefs.beanRoastType = BeanRoastType.MEDIUM
            R.id.bean_roast_dark -> filterPrefs.beanRoastType = BeanRoastType.DARK
        }
        viewModel.adapter.resetData()
    }

    private fun enableMenuItems(menuItem: MenuItem, value: Boolean) {
        menuItem.let {
            it.isEnabled = value
            (it.actionView as CompoundButton).isEnabled = value
        }
        viewModel.adapter.resetData()
    }

    companion object {
        fun getStartIntent(context: Context): Intent =
            Intent(context, HomeActivity::class.java)
    }
}