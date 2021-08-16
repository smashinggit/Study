package com.cs.library_architecture.utils

import android.graphics.*
import android.os.Build
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max


/**
 * @Author : ChenSen
 * @Date : 2019/12/18 17:26
 *
 * @Desc :
 */
object Bitmaps {


    fun decodeFile(imageFilePath: String, maxSize: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFilePath, options)
        options.inSampleSize = max(max(options.outWidth, options.outHeight) / maxSize, 1)

        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(imageFilePath, options)
    }


    fun decodeByteArray(data: ByteArray, maxSize: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(data, 0, data.size, options)
        options.inSampleSize = max(max(options.outWidth, options.outHeight) / maxSize, 1)

        options.inJustDecodeBounds = false
        return BitmapFactory.decodeByteArray(data, 0, data.size, options)
    }


    /**
     * 获取 [file] 的文件大小，即在磁盘上的物理大小
     */
    fun getFileSize(file: File, sizeType: SizeType = SizeType.KB): Long {
        return try {

            if (!file.exists() || file.isDirectory) {
                0
            } else {
                when (sizeType) {
                    SizeType.B -> file.length()
                    SizeType.KB -> file.length() / 1024
                    SizeType.MB -> file.length() / 1024 / 1024
                    SizeType.GB -> file.length() / 1024 / 1024 / 1024
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }


    /**
     * 获取 [bitmap] 占用内存大小
     */
    fun getMemorySize(bitmap: Bitmap, sizeType: SizeType = SizeType.KB): Int {
        val bytes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  //Since API 19
            bitmap.allocationByteCount
        } else {
            bitmap.byteCount
        }

        return when (sizeType) {
            SizeType.B -> bytes
            SizeType.KB -> bytes / 1024
            SizeType.MB -> bytes / 1024 / 1024
            SizeType.GB -> bytes / 1024 / 1024 / 1024
        }
    }

    /**
     * 通过计算获取占用内存的大小
     * 占用内存 = 宽 * 高 * 每个像素点占用的字节数
     *
     * 以 ARGB_8888 为例， 每个像素点占用4个字节，
     * 所以 占用内存 = 宽 * 高 * 4
     **/
    fun calculateMemorySize(bitmap: Bitmap, sizeType: SizeType = SizeType.KB): Int {

        val pixels = bitmap.width * bitmap.height
        val bytes = when (bitmap.config) {
            Bitmap.Config.ALPHA_8 -> pixels * 1
            Bitmap.Config.ARGB_4444 -> pixels * 2
            Bitmap.Config.ARGB_8888 -> pixels * 4
            Bitmap.Config.RGB_565 -> pixels * 2
            else -> pixels * 4
        }

        return when (sizeType) {
            SizeType.B -> bytes
            SizeType.KB -> bytes / 1024
            SizeType.MB -> bytes / 1024 / 1024
            SizeType.GB -> bytes / 1024 / 1024 / 1024
        }
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    fun bitmapToFile(bitmap: Bitmap, file: File): Boolean {
        val baos = ByteArrayOutputStream()
        val fileOutputStream = FileOutputStream(file)

        return try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            fileOutputStream.write(baos.toByteArray())
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            baos.close()
            fileOutputStream.close()
        }
    }


    /**
     * 将图片 [bitmap] 压缩到指定大小 [targetSize] 以内 ,单位是 kb
     * 这里的大小指的是 “文件大小”，而不是 “内存大小”
     *
     * 压缩图片(质量压缩)：
     * 1. 质量压缩不会减少图片的像素，它是在保持像素的前提下改变图片的位深及透明度，来达到压缩图片的目的，
     * 2. 压缩后图片的长，宽，像素都不会改变，那么 bitmap 所占内存大小是不会变的
     * 3. 由于图片的质量变低了，所以压缩后图片的大小会变小
     * 4. 质量压缩 png 格式这种图片没有作用，因为 png 是无损压缩
     */
    fun compressQuality(bitmap: Bitmap, targetSize: Int, declineQuality: Int = 10): ByteArray {

        val baos = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        log("压缩前文件大小：${baos.toByteArray().size / 1024} kb")

        var quality = 100
        while ((baos.toByteArray().size / 1024) > targetSize) {
            baos.reset()
            quality -= declineQuality
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        }

        log("压缩后文件大小：${baos.toByteArray().size / 1024} kb")

        return baos.toByteArray()
    }


    /**
     * 将图片 [byteArray] 压缩到 宽度小于 [targetWidth]、高度小于 [targetHeight]
     *
     * 压缩图片(采样率压缩)：
     * 1. 采样率压缩其原理是缩放 bitmap 的尺寸
     * 2. 压缩后图片的 宽度、高度以及占用的内存都会变小，文件大小也会变小(指压缩后保存到本地的文件，原始文件不会改变)
     * 3. 采样率 inSampleSize 代表 宽度、高度变为原来的几分之一，
     *    比如 inSampleSize 为 2，代表 宽度、高度都变为原来的 1/2，占用的内存就会变为原来的 1/4
     * 4. 采样率 inSampleSize 只能为 2 的整次幂，比如：2、4、16 ...
     *
     * 缺点： 由于 inSampleSize 只能为 2 的整次幂，所以无法精确控制大小
     *
     */
    fun compressInSampleSize(byteArray: ByteArray, targetWidth: Int, targetHeight: Int): ByteArray {

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)

        var inSampleSize = 1
        while (options.outWidth / inSampleSize > targetWidth || options.outHeight / inSampleSize > targetHeight) {
            inSampleSize *= 2
        }

        options.inJustDecodeBounds = false
        options.inSampleSize = inSampleSize
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)

        val compressedByreArray = bitmapToByteArray(bitmap)

        log("压缩前文件大小 ：${byteArray.size / 1024} kb")
        log("采样率 ：$inSampleSize ")
        log("压缩后文件大小 ：${compressedByreArray.size / 1024} kb")

        return compressedByreArray
    }


    /**
     * 缩放压缩
     * 将图片 [bitmap] 压缩到指定宽高范围内
     *
     * 压缩图片(缩放法压缩)：
     * 1. 放缩法压缩使用的是通过矩阵对图片进行裁剪
     * 2. 缩放后图片的 宽度、高度以及占用的内存都会变小，文件大小也会变小(指压缩后保存到本地的文件，原始文件不会改变)
     *
     */
    fun compressScale(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        return try {
            val scale = Math.min(targetWidth * 1.0f / bitmap.width, targetHeight * 1.0f / bitmap.height)

            val matrix = Matrix()
            matrix.setScale(scale, scale)

            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, (bitmap.width * scale).toInt(), (bitmap.height * scale).toInt(), true)

            val rawBytes = bitmapToByteArray(bitmap)
            val scaledBytes = bitmapToByteArray(scaledBitmap)
            log("压缩前文件大小 ：${rawBytes.size / 1024} kb")
            log("缩放率 ：$scale ")
            log("压缩后文件大小 ：${scaledBytes.size / 1024} kb")

            scaledBitmap
        } catch (e: Exception) {
            e.printStackTrace()
            bitmap
        }
    }

    /**
     * 将图片格式更改为 Bitmap.Config.RGB_565，减少图片占用的内存大小
     *
     * 1.  由于图片的存储格式改变，与 ARGB_8888 相比，每个像素的占用的字节更少
     *     所以整体图片占用的内存也变小
     *
     * 2. 如果图片不包含透明信息的话，可以使用此方法进行压缩
     */
    fun compressRGB565(byteArray: ByteArray): Bitmap {

        return try {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.RGB_565
            val compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)

            log("压缩前文件大小 ：${byteArray.size / 1024} kb")
            log("压缩后文件大小 ：${byteArray.size / 1024} kb")
            compressedBitmap
        } catch (e: Exception) {
            e.printStackTrace()
            BitmapFactory.decodeByteArray(ByteArray(0), 0, 0)
        }
    }


    /**
     * 水平镜像
     */
    fun mirrorX(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.setScale(-1f, 1f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
    }

    /**
     * 竖直镜像
     */
    fun mirrorY(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.setScale(1f, -1f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
    }

    /**
     * 旋转
     *
     * 注意：如果 [degree] 不是90的倍数的话，会导致旋转后图片变成"斜的"，
     * 然而此时计算图片的宽高时仍然是按照水平和竖直方向计算，所以会导致最终旋转后的图片变大
     * 如果进行多次旋转的话，最终会出现OMM
     */
    fun rotate(bitmap: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
    }


    /**
     * 根据比例 [scale] 缩放
     */
    fun scale(bitmap: Bitmap, scale: Float): Bitmap {
        return if (scale == 1f || scale <= 0) {
            bitmap
        } else {
            Bitmap.createScaledBitmap(bitmap, (bitmap.width * scale).toInt(), (bitmap.height * scale).toInt(), false)
        }
    }

    /**
     * 根据指定宽 [newWidth] 、高 [newHeight] 进行缩放
     */
    fun scale(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        return if (newWidth <= 0 || newHeight <= 0) {
            bitmap
        } else {
            val scaleX = newWidth.toFloat() / bitmap.width
            val scaleY = newHeight.toFloat() / bitmap.height
            val matrix = Matrix()
            matrix.postScale(scaleX, scaleY)
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
        }
    }

    /**
     * 从图片中间位置裁剪出一个宽高为的 [width] [height]图片
     */
    fun crop(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return if (bitmap.width < width || bitmap.height < height) {
            bitmap
        } else {
            Bitmap.createBitmap(bitmap, (bitmap.width - width) / 2, (bitmap.height - height) / 2, width, height)
        }
    }

    /**
     * 从图片中间位置裁剪出一个半径为 [radius] 的圆形图片
     */
    fun cropCircle(bitmap: Bitmap, radius: Int): Bitmap {

        val realRadius: Int = if (bitmap.width / 2 < radius || bitmap.height / 2 < radius) {
            Math.min(bitmap.width, bitmap.height) / 2
        } else {
            radius
        }

        val src = crop(bitmap, realRadius * 2, realRadius * 2)
        val circle = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(circle)
        canvas.drawARGB(0, 0, 0, 0)
        val paint = Paint()
        paint.isAntiAlias = true

        canvas.drawCircle((circle.width / 2).toFloat(), (circle.height / 2).toFloat(), realRadius.toFloat(), paint)

        val rect = Rect(0, 0, circle.width, circle.height)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(src, rect, rect, paint)

        return circle
    }


    /**
     * 将图片转换成 ByteArray
     */
    fun pngToBytes(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    /**
     * 将图片转换成 ByteArray
     */
    fun jpgToBytes(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun nv21ToJpeg(data: ByteArray, width: Int, height: Int): ByteArray? {
        return try {
            val image = YuvImage(data, ImageFormat.NV21, width, height, null)
            val stream = ByteArrayOutputStream()
            image.compressToJpeg(Rect(0, 0, width, height), 100, stream)
            val result = stream.toByteArray()
            stream.close()
            result
        } catch (ex: Exception) {
            null
        }
    }


    enum class SizeType {
        B,
        KB,
        MB,
        GB
    }

    fun log(msg: String) {
        LogUtils.log(msg)
    }
}