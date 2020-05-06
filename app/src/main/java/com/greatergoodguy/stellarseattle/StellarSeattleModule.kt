package com.greatergoodguy.stellarseattle

import com.greatergoodguy.stellarseattle.activity.MainActivity
import com.greatergoodguy.stellarseattle.activity.VenueDetailsActivity
import com.greatergoodguy.stellarseattle.adapter.VenueAdapter
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class  StellarSeattleModule {

    @ContributesAndroidInjector
    abstract fun provideInjectorMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun provideInjectorVenueDetailsActivity(): VenueDetailsActivity

    @ContributesAndroidInjector
    abstract fun provideInjectorVenueAdapter(): VenueAdapter
}
