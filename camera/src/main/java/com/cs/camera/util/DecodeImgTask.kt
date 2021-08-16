package com.cs.camera.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import java.lang.ref.WeakReference
import java.util.*

/**
 *
 * author : ChenSen
 * data : 2018/3/16
 * desc:
 */
class DecodeImgTask(imageView: ImageView) : AsyncTask<String, Int, Bitmap?>() {
    private val imageViewReference: WeakReference<ImageView> = WeakReference(imageView)
    private var temp = 0L

    override fun doInBackground(vararg params: String?): Bitmap? {
        Log.d("tag", "图片保存位置 ${params.contentToString()}")
        temp = System.currentTimeMillis()
        return BitmapFactory.decodeFile(params[0])
    }

    override fun onPostExecute(bitmap: Bitmap?) {
        if (imageViewReference != null && bitmap != null) {
            imageViewReference.get()?.let {
                val compressBitmap = BitmapUtils.decodeBitmap(bitmap, 720, 1080)
                it.setImageBitmap(compressBitmap)
                Log.d("tag", "原始图片大小  ${bitmap.width} * ${bitmap.height}")
                Log.d("tag", "压缩后图片大小  ${compressBitmap.width} * ${compressBitmap.height}")
                Log.d("tag", "加载图片耗时 ${System.currentTimeMillis() - temp}")
            }
        }
        super.onPostExecute(bitmap)
    }
}