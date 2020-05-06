package com.greatergoodguy.stellarseattle.presentation

import androidx.lifecycle.ViewModel
import com.greatergoodguy.stellarseattle.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class PresentationModule {

    @Scope
    @ContributesAndroidInjector
    abstract fun provideInjectorMainActivity(): MainActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}

@Scope
annotation class Scope
