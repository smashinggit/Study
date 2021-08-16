package com.cs.app.life

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.cs.app.App
import java.util.*

/**
 *
 * author : ChenSen
 * data : 2019/6/5
 * desc:
 *
 * 1. 可以控制应用中某个Activity被初始化的数量，如商城应用中的商品详情页，避免无限制的创建Activity
 * 2. 判断应用是否处于前台可见状态，通过onActivityResumed和onActivityPaused回调方法实现
 */
object MyActivityManager : Application.ActivityLifecycleCallbacks {

    val mActivityStack = Stack<Activity>()
    var mActivityCount = 0
    var isForground = false

    /**
     * 入栈
     */
    fun addActivity(activity: Activity) {
        mActivityStack.add(activity)
    }

    /**
     * 出栈
     */
    fun removeActivity(activity: Activity) {
        mActivityStack.remove(activity)
    }

    /**
     * 结束某Activity
     */

    fun finishActivity(activity: Activity) {
        removeActivity(activity)
        activity.finish()
    }


    /**
     * 获取当前Activity
     */
    fun getCurrentActivity(): Activity {
        return mActivityStack.lastElement()
    }


    /**
     * 结束当前Activity
     */
    fun finishCurrentActivity() {
        finishActivity(mActivityStack.lastElement())
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        log("onActivitySaveInstanceState ${activity.componentName}")
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        log("onActivityCreated ${activity.componentName}")
        activity.apply { mActivityStack.add(this) }
    }

    override fun onActivityStarted(activity: Activity) {
        log("onActivityStarted ${activity.componentName}")
        mActivityCount++
        isForground = true
    }

    override fun onActivityResumed(activity: Activity) {
        log("onActivityResumed ${activity.componentName}")
    }

    override fun onActivityPaused(activity: Activity) {
        log("onActivityPaused ${activity.componentName}")
    }

    override fun onActivityStopped(activity: Activity) {
        log("onActivityStopped ${activity.componentName}")

        mActivityCount--
        if (mActivityCount == 0) {
            isForground = false
            Toast.makeText(App.INSTANCE, "程序进入后台", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        log("onActivityDestroyed ${activity.componentName}")
        activity.apply { removeActivity(activity) }
    }

    fun log(msg: String) {
//        Log.e("tag", "$msg")
    }
}