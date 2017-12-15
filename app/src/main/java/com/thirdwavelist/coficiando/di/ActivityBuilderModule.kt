package com.thirdwavelist.coficiando.di

import com.thirdwavelist.coficiando.features.details.DetailsActivity
import com.thirdwavelist.coficiando.features.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): HomeActivity

    @ContributesAndroidInjector
    abstract fun contributeDetailsActivity(): DetailsActivity

}