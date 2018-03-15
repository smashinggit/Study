package com.cs.camerademo.util

import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * author :  chensen
 * data  :  2018/3/15
 * desc :
 */
object FileUtil {
    private val rootFolderPath = Environment.getExternalStorageDirectory().absolutePath + File.separator + "CameraDemo"

    fun createImageFile(): File? {
        return try {
            var rootFile = File(rootFolderPath)
            if (!rootFile.exists())
                rootFile.mkdirs()

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val fileName = "IMG_$timeStamp.jpg"
            File(rootFolderPath + File.separator + fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}