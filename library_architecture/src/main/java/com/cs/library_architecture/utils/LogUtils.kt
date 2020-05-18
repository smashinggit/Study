package com.cs.library_architecture.utils

import android.util.Log

object LogUtils {

    private const val TAG = "tag"

    fun log(msg: String) {
        Log.e(TAG, msg)
    }

    fun log(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    fun logd(msg: String) {
        Log.d(TAG, msg)
    }

    fun logd(tag: String, msg: String) {
        Log.d(tag, msg)
    }

}