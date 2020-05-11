package com.cs.library_architecture.utils

import android.graphics.*
import android.os.Build
import java.io.ByteArrayOutputStream
import kotlin.math.max


/**
 * @Author : ChenSen
 * @Date : 2019/12/18 17:26
 *
 * @Desc :
 */
class BitmapUtils {

    companion object {

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
         * 获取 [bitmap] 大小
         */
        fun getSize(bitmap: Bitmap): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  //Since API 19
                bitmap.allocationByteCount
            } else {
                bitmap.rowBytes * bitmap.height     // Bitmap所占用的内存空间数等于Bitmap的每一行所占用的空间数乘以Bitmap的行数
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

    }
}