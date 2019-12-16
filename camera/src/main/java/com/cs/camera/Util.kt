package com.cs.camera

import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * author :  chensen
 * data  :  2018/3/18
 * desc :
 */

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun log(msg: String) {
    Log.e("tag", msg)
}