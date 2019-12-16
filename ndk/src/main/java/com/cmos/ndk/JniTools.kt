package com.cmos.ndk

/**
 *
 * author : ChenSen
 * data : 2018/10/30
 * desc:
 */
object JniTools {
    external fun getStringFromNDK(): String

    init {
        System.loadLibrary("config")
    }
}