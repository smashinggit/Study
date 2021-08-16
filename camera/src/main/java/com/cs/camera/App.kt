package com.cs.camera

import android.app.Application
import android.os.Environment
import android.util.Log
import java.io.File

/**
 * @Author : ChenSen
 * @Date : 2019/9/25 14:58
 *
 * @Desc :
 */
class App : Application() {

    companion object {
        var INSTANCE: Application? = null

        fun getRootPath(): String {
            return INSTANCE?.getExternalFilesDir(null)?.absolutePath ?: ""
        }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        val rootFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + File.separator + "CameraDemo"

        val file = File("/storage/emulated/0/Download")
        if (!file.exists()) {
            Log.d("tag", "  file.mkdirs() ${file.absolutePath}")
            file.mkdirs()
        }

        val file2 = File(file, "text.txt")
        file2.createNewFile()
    }
}