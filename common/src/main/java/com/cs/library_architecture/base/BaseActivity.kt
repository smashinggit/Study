package com.cs.library_architecture.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


/**
 * author : ChenSen
 * data : 2019/5/30
 * desc:
 */
open class BaseActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun log(msg: String) {
        Log.e("tag", msg)
    }

    protected fun logd(msg: String) {
        Log.d("tag", msg)
    }

    protected fun loge(msg: String) {
        Log.e("tag", msg)
    }

    protected fun log(tag: String, msg: String) {
        Log.e(tag, msg)
    }

}
