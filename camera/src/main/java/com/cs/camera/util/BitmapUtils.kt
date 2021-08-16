package com.cs.camera.util

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.provider.MediaStore
import com.cs.camera.log
import okio.buffer
import okio.sink
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

/**
 *
 * author : ChenSen
 * data : 2018/3/16
 * desc:
 */
object BitmapUtils {

    fun toByteArray(bitmap: Bitmap): ByteArray {
        val os = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        return os.toByteArray()
    }

    fun mirror(rawBitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postScale(-1f, 1f)
        return Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.width, rawBitmap.height, matrix, true)
    }

    fun rotate(rawBitmap: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.width, rawBitmap.height, matrix, true)
    }

    fun decodeBitmap(bitmap: Bitmap, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        BitmapFactory.decodeByteArray(bos.toByteArray(), 0, bos.size(), options)

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeByteArray(bos.toByteArray(), 0, bos.size(), options)
    }

    fun decodeBitmapFromFile(path: String, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }

    fun decodeBitmapFromResource(res: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val rawWidth = options.outWidth
        val rawHeight = options.outHeight
        var inSampleSize = 1

        if (rawWidth > reqWidth || rawHeight > reqHeight) {
            val halfWidth = rawWidth / 2
            val halfHeight = rawHeight / 2

            while ((halfWidth / inSampleSize) > reqWidth && (halfHeight / inSampleSize) > reqHeight) {
                inSampleSize *= 2  //设置inSampleSize为2的幂是因为解码器最终还是会对非2的幂的数进行向下处理，获取到最靠近2的幂的数
            }
        }
        return inSampleSize
    }

    fun savePic(data: ByteArray?, folderName: String = "camera1", isMirror: Boolean = false, onSuccess: (savedPath: String, time: String) -> Unit, onFailed: (msg: String) -> Unit) {
        thread {
            try {
                val temp = System.currentTimeMillis()
                val picFile = FileUtil.createCameraFile(folderName)
                if (picFile != null && data != null) {

                    val rawBitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                    val resultBitmap = if (isMirror) mirror(rawBitmap) else rawBitmap
                    picFile.sink().buffer().write(toByteArray(resultBitmap)).close()
                    onSuccess(picFile.absolutePath, "${System.currentTimeMillis() - temp}")

                    log("图片已保存! 耗时：${System.currentTimeMillis() - temp}    路径：  ${picFile.absolutePath}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onFailed("${e.message}")
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun savePicToPublicStorage(context: Context, data: ByteArray?, isMirror: Boolean = false, onSuccess: (savedPath: String, time: String) -> Unit, onFailed: (msg: String) -> Unit) {
        thread {
            try {
                val temp = System.currentTimeMillis()

                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val fileName = "IMG_$timeStamp.jpg"

                // 创建图片索引
                val value = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                    put(MediaStore.Images.Media.DATE_ADDED, temp)
                }

                // 将该索引信息插入数据表，获得图片的Uri
                // 保存的路径在 /sdcard/Pictures
                val imageUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value)

                if (imageUri != null && data != null) {
                    //通过图片uri获得输出流
                    val outputStream = context.contentResolver.openOutputStream(imageUri)

                    val rawBitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                    val resultBitmap = if (isMirror) mirror(rawBitmap) else rawBitmap
                    resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    log("图片已保存! 耗时：${System.currentTimeMillis() - temp}    路径：  ${imageUri.path}")

                    onSuccess(imageUri.path ?: "", "${System.currentTimeMillis() - temp}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onFailed("${e.message}")
            }
        }
    }

}