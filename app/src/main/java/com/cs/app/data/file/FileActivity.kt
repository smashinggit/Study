package com.cs.app.data.file

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.annotation.RequiresApi
import com.cs.app.R
import com.cs.common.base.BaseActivity
import kotlinx.android.synthetic.main.activity_file.*
import java.io.File

/**
 *
 * author : ChenSen
 * data : 2019/7/16
 * desc:
 *
 * 内部存储：
 * getFilesDir()
 *
 * getCacheDir()  //此目录应该限制大小，并定期清除
 *
 * openFileOutput()
 *
 *
 * 外部存储：
 *
 *
 *
 */
class FileActivity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        createInternalFile()  //内部文件

        createExternalFile()   //外部文件

    }


    @SuppressLint("SetTextI18n")
    private fun createInternalFile() {
        tvInternal.text = "filesDir -> ${filesDir.absolutePath}\n" +
                "cacheDir -> ${cacheDir.absolutePath}"

        //内部存储：
        val privateFile = File(filesDir, "privateFile").apply {
            if (!exists())
                createNewFile()
            writeText("这是私有文件")
        }


        val cacheFile = File(cacheDir, "cacheFile").apply {
            if (!exists())
                createNewFile()
            writeText("这是缓存文件，需要定期清除")
        }

        val fileOutput = openFileOutput("myFile", Context.MODE_PRIVATE)
                .use { it.write("Hello World".toByteArray()) }

    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    private fun createExternalFile() {
        tvExternal.text = "Environment.getExternalStorageDirectory ->   ${Environment.getExternalStorageDirectory().absolutePath}\n" +
                "Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) ->   ${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath}\n" +
                "Context#getExternalCacheDirs() ->   ${externalCacheDir.absolutePath}\n" +
                "Context#getExternalFilesDir(Environment.DIRECTORY_PICTURES) ->   ${getExternalFilesDir(Environment.DIRECTORY_PICTURES).absolutePath}\n"

        Environment.getExternalStorageDirectory()
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)


        //这两个方法返回的是私有空间，媒体不可见
        getExternalFilesDir(null)
                .also {
                    val externalPrivateFile = File(it, "externalPrivateFile")
                    if (!externalPrivateFile.exists())
                        externalPrivateFile.createNewFile()

                    externalPrivateFile.writeText("在外部存储中的私有数据，媒体看不到哦")
                }
        getExternalFilesDirs(Environment.DIRECTORY_DOCUMENTS)

    }

    /* Checks if external storage is available for read and write */
    fun isExternalStorageWritable() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    /* Checks if external storage is available to at least read */
    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

}