package com.cs.app

import android.app.Application
import android.content.Context
import android.util.Log
import com.cs.app.life.MyActivityManager

/**
 *
 * author : ChenSen
 * data : 2019/6/5
 * desc:
 */
class App : Application() {

    companion object {
        lateinit var INSTANCE: App
    }


    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        registerActivityLifecycleCallbacks(MyActivityManager)
    }
}

fun Context.log(msg: String) {
    Log.d("tag", msg)
}