package com.cs.bitmap

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_bigpic.*

/**
 *
 * author : ChenSen
 * data : 2019/5/29
 * desc:  加载超清大图
 */
class BigPicActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bigpic)

        val inputStream = assets.open("qmsht.jpg")
        ivLargeImageView.setInputStream(inputStream)


        //显示图片的某一部分
        // test()

    }


    /**
     *  显示图片的某一部分
     */
    private fun test() {
        try {
            val inputStream = assets.open("pic2.jpg")

            //获得图片的宽、高
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(inputStream, null, options)
            val width = options.outWidth
            val height = options.outHeight
//            log("原图 ：$width * $height")


            //设置显示图片的某一区域
            val regionDecoder = BitmapRegionDecoder.newInstance(inputStream, false)
            val rect = Rect(width / 2 - 200, height / 2 - 200, width / 2 + 200, height / 2 + 200)
            val options2 = BitmapFactory.Options()
            options2.inPreferredConfig = Bitmap.Config.RGB_565
            val bitmap = regionDecoder.decodeRegion(rect, options2)

            ivPic2.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}