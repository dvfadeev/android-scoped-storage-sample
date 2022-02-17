package com.sample.scoped_storage.core.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.sample.scoped_storage.MainActivity

class ActivityProvider(app: com.sample.scoped_storage.App) {

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