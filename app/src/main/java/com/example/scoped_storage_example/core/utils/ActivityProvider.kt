package com.example.scoped_storage_example.core.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.scoped_storage_example.App
import com.example.scoped_storage_example.MainActivity

class ActivityProvider(app: App) {

    private var activeActivity: Activity? = null

    init {
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activeActivity = activity
            }

            override fun onActivityDestroyed(activity: Activity) {
                activeActivity = null
            }

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        })
    }

    fun getActivity(): MainActivity {
        return activeActivity as MainActivity
    }
}