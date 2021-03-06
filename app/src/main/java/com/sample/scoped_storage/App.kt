package com.sample.scoped_storage

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.sample.scoped_storage.core.KoinProvider
import com.sample.scoped_storage.core.utils.ComponentFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.dsl.koinApplication

class App : Application(), KoinProvider {

    override lateinit var koin: Koin
        private set

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        koin = koinApplication {
            androidContext(this@App)
            modules(allModules)
        }.koin

        koin.declare(ComponentFactory(koin))
    }
}