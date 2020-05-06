package com.greatergoodguy.stellarseattle.di

import com.greatergoodguy.stellarseattle.StellarSeattleApplication
import com.greatergoodguy.stellarseattle.api.APIModule
import com.greatergoodguy.stellarseattle.presentation.PresentationModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    PresentationModule::class,
    APIModule::class,
    ViewModelModule::class
])
interface AppComponent : AndroidInjector<StellarSeattleApplication>
