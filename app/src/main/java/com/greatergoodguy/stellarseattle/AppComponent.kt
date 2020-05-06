package com.greatergoodguy.stellarseattle

import com.greatergoodguy.stellarseattle.StellarSeattleApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@Component(modules = [
    AndroidInjectionModule::class,
    StellarSeattleModule::class
])
interface AppComponent : AndroidInjector<StellarSeattleApplication>
