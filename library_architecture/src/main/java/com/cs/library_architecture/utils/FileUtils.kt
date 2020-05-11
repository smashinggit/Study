package com.cs.library_architecture.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import okio.buffer
import okio.sink
import java.io.File
import java.io.FileOutputStream

/**
 * @Author : ChenSen
 * @Date : 2019/12/18 17:26
 *
 * @Desc :
 */
class FileUtils {

    companion object {

        /**
         * 外部存储中的私有目录
         *
         * 里面的内容会随应用的卸载而删除
         */
        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun externalPrivateDirs(context: Context): String {
            //storage/emulated/0/Android/data/包名/files
            return context.getExternalFilesDirs(null)[0].absolutePath
        }

        /**
         * 公共目录
         *
         * 这些目录的内容不会随着应用的卸载而消失
         */
        fun externalPublicDirs(context: Context): String {
            //storage/emulated/0/
            return Environment.getExternalStorageDirectory().absolutePath
        }


        fun createFile(pathName: String): File {
            val file = File(pathName)
            val parent = file.parentFile

            if (!parent.exists()) {
                parent.mkdirs()
            }

            if (!file.exists()) {
                file.createNewFile()
            }
            return file
        }


        fun createFile(parentPath: String, fileName: String): File {
            val file = File(parentPath, fileName)
            val parent = File(parentPath)

            if (!parent.exists()) {
                parent.mkdirs()
            }

            if (!file.exists()) {
                file.createNewFile()
            }
            return file
        }

        fun writeToFile(data: ByteArray, file: File) {
            val fileOutputStream = FileOutputStream(file)
            try {
                fileOutputStream.write(data)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                fileOutputStream.flush()
            }
        }

        /**
         * 将 [data] 写入到 [file]中
         * 如果 [appending] 为 true ,则是追加到 [file] 的内容之后，否则覆盖
         */
        fun writeToFile2(data: ByteArray, file: File, appending: Boolean = false) {

            try {
                file.sink(appending).buffer().write(data).close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         *
         * 获取文件大小
         *
         * 1 KB = 1024 Bytes
         * 1 MB = 1024 KB
         * 1 G = 1024 MB
         */
        fun fileSize(file: File, sizeType: SizeType = SizeType.MB): Long {

            return try {

                if (!file.exists() || file.isDirectory) {
                    0
                } else {
                    when (sizeType) {
                        SizeType.B -> file.length()
                        SizeType.KB -> file.length() / 1024
                        SizeType.MB -> file.length() / 1024 / 1024
                        SizeType.G -> file.length() / 1024 / 1024 / 1024
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }
    }


    enum class SizeType {
        B,
        KB,
        MB,
        G
    }
}