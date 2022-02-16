package com.example.scoped_storage_example

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.scoped_storage_example.core.KoinProvider
import com.example.scoped_storage_example.core.utils.ActivityProvider
import com.example.scoped_storage_example.core.utils.ComponentFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.core.context.loadKoinModules
import org.koin.dsl.koinApplication
import org.koin.dsl.module

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