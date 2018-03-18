package com.cs.camerademo

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast

/**
 * author :  chensen
 * data  :  2018/3/18
 * desc :
 */
open class BaseActivity : AppCompatActivity() {

    fun Context.toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun Context.log(msg: String) {
        Log.e("tag", msg)
    }
}