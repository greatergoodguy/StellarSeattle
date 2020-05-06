package com.greatergoodguy.stellarseattle.activity.di

import com.greatergoodguy.stellarseattle.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class Module {
    @ContributesAndroidInjector
    abstract fun provideInjectorMainActivity(): MainActivity

}
