package com.cs.library_architecture.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Author : ChenSen
 * @Date : 2019/12/25 15:52
 *
 * @Desc :
 */
class DateUtils {

    companion object {

        @SuppressLint("SimpleDateFormat")
        fun currentTime(pattern: String = "yyyy年MM月dd日 HH:mm:ss"): String {
            val date = Date()
            val format = SimpleDateFormat(pattern)
            return format.format(date)
        }
    }
}