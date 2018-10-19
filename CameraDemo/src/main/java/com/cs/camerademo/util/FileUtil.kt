package com.cs.camerademo.util

import android.annotation.SuppressLint
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

    fun createImageFile(isCrop: Boolean = false): File? {
        return try {
            var rootFile = File(rootFolderPath + File.separator + "capture")
            if (!rootFile.exists())
                rootFile.mkdirs()

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val fileName = if (isCrop) "IMG_${timeStamp}_CROP.jpg" else "IMG_$timeStamp.jpg"
            File(rootFile.absolutePath + File.separator + fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun createCameraFile(): File? {
        return try {
            var rootFile = File(rootFolderPath + File.separator + "camera1")
            if (!rootFile.exists())
                rootFile.mkdirs()

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val fileName = "IMG_$timeStamp.jpg"
            File(rootFile.absolutePath + File.separator + fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun createVideoFile(): File? {
        return try {
            var rootFile = File(rootFolderPath + File.separator + "video")
            if (!rootFile.exists())
                rootFile.mkdirs()

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val fileName = "VIDEO_$timeStamp.mp4"
            File(rootFile.absolutePath + File.separator + fileName)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}