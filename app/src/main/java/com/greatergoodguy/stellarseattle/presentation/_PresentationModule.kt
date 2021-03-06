package com.greatergoodguy.stellarseattle.presentation

import androidx.lifecycle.ViewModel
import com.greatergoodguy.stellarseattle.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class PresentationModule {

    @ContributesAndroidInjector
    abstract fun provideInjectorVenueDetailsActivity(): VenueDetailsActivity

    @ContributesAndroidInjector
    abstract fun provideInjectorMapActivity(): MapActivity

    @Scope
    @ContributesAndroidInjector
    abstract fun provideInjectorMainActivity(): MainActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(VenueDetailsViewModel::class)
    internal abstract fun bindVenueDetailsViewModel(viewModel: VenueDetailsViewModel): ViewModel
}

@Scope
annotation class Scope
