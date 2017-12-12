package com.thirdwavelist.coficiando.di

import com.thirdwavelist.coficiando.features.home.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): HomeActivity

}