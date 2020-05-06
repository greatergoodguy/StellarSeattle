package com.greatergoodguy.stellarseattle

import com.greatergoodguy.stellarseattle.StellarSeattleApplication
import com.greatergoodguy.stellarseattle.api.APIModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@Component(modules = [
    AndroidInjectionModule::class,
    StellarSeattleModule::class,
    APIModule::class
])
interface AppComponent : AndroidInjector<StellarSeattleApplication>
