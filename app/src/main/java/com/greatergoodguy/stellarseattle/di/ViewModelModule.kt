package com.greatergoodguy.stellarseattle.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

/*
 * This needs to be in a separate module since you can't mix @Provides and @Binds in the same module
 */
@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
