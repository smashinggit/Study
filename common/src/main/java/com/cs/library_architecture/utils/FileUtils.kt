package com.cs.library_architecture.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import okio.buffer
import okio.sink
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL

/**
 * @Author : ChenSen
 * @Date : 2019/12/18 17:26
 *
 * @Desc :
 */
object FileUtils {


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


    /**
     * 注意：不适合读取大文件
     */
    fun readFormUrl(url: URL) {
        val bytes = url.readBytes()


    }

    fun writeToFile(inputStream: InputStream, file: File) {

        val fileOutputStream = FileOutputStream(file)
        val bytes = ByteArray(1024)

        try {
            do {
                val readLength = inputStream.read(bytes)
                if (readLength != -1) {
                    fileOutputStream.write(bytes, 0, readLength)
                } else {
                    break
                }
            } while (true)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream.close()
            fileOutputStream.close()
        }
    }

    fun writeToFile(data: ByteArray, file: File) {
        val fileOutputStream = FileOutputStream(file)
        try {
            fileOutputStream.write(data)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            fileOutputStream.close()
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
     * 获取文件大小
     */
    fun fileSize(file: File, sizeType: SizeType = SizeType.KB): Long {
        return try {
            val size = file.length()

            when (sizeType) {
                SizeType.B -> size
                SizeType.KB -> size / 1024
                SizeType.MB -> size / 1024 / 1024
                SizeType.GB -> size / 1024 / 1024 / 1024
            }

        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    /**
     * 获取文件大小
     * 因为 available()的返回值类型为 int 型，可能会存在溢出的情况
     * 所以 available()方法计算文件大小不建议使用
     */
    fun fileSize2(file: File, sizeType: SizeType = SizeType.KB): Int {
        val fileInputStream = FileInputStream(file)

        return try {
            val size = fileInputStream.available()

            when (sizeType) {
                SizeType.B -> size
                SizeType.KB -> size / 1024
                SizeType.MB -> size / 1024 / 1024
                SizeType.GB -> size / 1024 / 1024 / 1024
            }

        } catch (e: Exception) {
            e.printStackTrace()
            -1
        } finally {
            fileInputStream.close()
        }
    }

    /**
     * 获取文件大小
     * 通过 FileChannel 类来获取文件大小，这个方法通常结合输入流相关，
     * 因此可以用于文件网络传输时实时计算文件大小
     */
    fun fileSize3(file: File, sizeType: SizeType = SizeType.KB): Long {
        val fileInputStream = FileInputStream(file)

        return try {
            val size = fileInputStream.channel.size()

            when (sizeType) {
                SizeType.B -> size
                SizeType.KB -> size / 1024
                SizeType.MB -> size / 1024 / 1024
                SizeType.GB -> size / 1024 / 1024 / 1024
            }

        } catch (e: Exception) {
            e.printStackTrace()
            -1
        } finally {
            fileInputStream.close()
        }
    }


    enum class SizeType {
        B,
        KB,
        MB,
        GB
    }
}