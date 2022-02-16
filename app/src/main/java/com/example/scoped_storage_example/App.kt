package com.example.scoped_storage_example

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.scoped_storage_example.core.KoinProvider
import com.example.scoped_storage_example.core.utils.ComponentFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.dsl.koinApplication

class App : Application(), KoinProvider {
    override lateinit var koin: Koin
        private set

    var activeActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        koin = koinApplication {
            androidContext(this@App)
            modules(allModules)
        }.koin

        koin.declare(ComponentFactory(koin))

        registerActivityListener()
    }

    private fun registerActivityListener() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
                activeActivity = null
            }

            override fun onActivityResumed(activity: Activity) {
                activeActivity = activity
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}