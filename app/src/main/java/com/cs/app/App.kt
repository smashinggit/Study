package com.cs.app

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.multidex.MultiDexApplication
import com.cs.app.life.MyActivityManager

/**
 *
 * author : ChenSen
 * data : 2019/6/5
 * desc:
 */
class App : MultiDexApplication() {

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

fun Context.taost(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}