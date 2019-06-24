package com.cs.architecture

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.cs.common.utils.LogUtils

/**
 *
 * author : ChenSen
 * data : 2019/6/21
 * desc:
 */
class ActivityLifeCycleListener : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(p0: Activity?) {

        p0?.apply {
            LogUtils.logi(componentName.className + "-> onActivityPaused")
        }

    }

    override fun onActivityResumed(p0: Activity?) {
        p0?.apply {
            LogUtils.logi(componentName.className + "-> onActivityResumed")
        }
    }

    override fun onActivityStarted(p0: Activity?) {
        p0?.apply {
            LogUtils.logi(componentName.className + "-> onActivityStarted")
        }
    }

    override fun onActivityDestroyed(p0: Activity?) {
        p0?.apply {
            LogUtils.logi(componentName.className + "-> onActivityDestroyed")
        }
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
        p0?.apply {
            LogUtils.logi(componentName.className + "-> onActivitySaveInstanceState")
        }
    }

    override fun onActivityStopped(p0: Activity?) {
        p0?.apply {
            LogUtils.logi(componentName.className + "-> onActivityStopped")
        }
    }

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
        p0?.apply {
            LogUtils.logi(componentName.className + "-> onActivityCreated")
        }
    }

}