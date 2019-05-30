package com.cs.glidedemo

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_bitmap.*

/**
 *
 * author : ChenSen
 * data : 2019/5/29
 * desc:
 */
class BitmapActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitmap)


        btnFromDrawable.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.pic2)
            log("bitmap:  ${bitmap.width} * ${bitmap.height}   ${bitmap.byteCount} ")

            ivPic.setImageBitmap(bitmap)
        }

        btnCompress.setOnClickListener {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeResource(resources, R.drawable.pic2, options)
            log("压缩前 mOptions:  ${options.outWidth} * ${options.outHeight}   ${options.outMimeType} ")

            //todo 我们可以根据宽和高的大小来决定压缩多少(此处省略)

            options.inSampleSize = 2 //这里直接把宽和高变为原来的一半
            options.inJustDecodeBounds = false

            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.pic2, options)
            ivPic.setImageBitmap(bitmap)

            log("压缩后 bitmap:  ${bitmap.width} * ${bitmap.height}   ${bitmap.byteCount} ")
        }


        //加载超清大图
        btnBigPic.setOnClickListener { startActivity(Intent(this, BigPicActivity::class.java)) }
    }


    fun Activity.log(msg: String) {
        Log.e("tag", msg)
    }
}