package com.cs.common.utils

import android.content.Context
import android.widget.Toast

/**
 *
 * author : ChenSen
 * data : 2019/7/23
 * desc:
 */


fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
