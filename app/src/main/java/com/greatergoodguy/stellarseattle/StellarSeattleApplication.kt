package com.greatergoodguy.stellarseattle

import android.app.Application
import com.greatergoodguy.stellarseattle.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class StellarSeattleApplication : Application(), HasAndroidInjector {

    @Inject
    internal lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.create().inject(this)
    }
}
