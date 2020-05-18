package com.cs.bitmap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.cs.library_architecture.base.BaseActivity
import com.cs.library_architecture.utils.Bitmaps
import kotlinx.android.synthetic.main.activity_function.*

/**
 * @Author : ChenSen
 * @Date : 2019/12/20 14:41
 *
 * @Desc :
 */
class FunctionActivity : BaseActivity() {

    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_function)


        bitmap = BitmapFactory.decodeStream(assets.open("pic2.jpg"))
        ivPic.setImageBitmap(bitmap)
        log("bitmap ${bitmap.width} * ${bitmap.height}")

        btMirrorX.setOnClickListener {
            bitmap = Bitmaps.mirrorX(bitmap)
            ivPic.setImageBitmap(bitmap)
        }

        btMirrorY.setOnClickListener {
            bitmap = Bitmaps.mirrorY(bitmap)
            ivPic.setImageBitmap(bitmap)
        }

        btRoate.setOnClickListener {
            bitmap = Bitmaps.rotate(bitmap, 90f)
            log("bitmap ${bitmap.width} * ${bitmap.height}")
            ivPic.setImageBitmap(bitmap)
        }

        btnScale.setOnClickListener {
            bitmap = Bitmaps.scale(bitmap, 0.5f)
            log("bitmap ${bitmap.width} * ${bitmap.height}")
            ivPic.setImageBitmap(bitmap)
        }

        btnScaleToSize.setOnClickListener {
            bitmap = Bitmaps.scale(bitmap, 300, 500)
            log("bitmap ${bitmap.width} * ${bitmap.height}")
            ivPic.setImageBitmap(bitmap)
        }

        btnCorp.setOnClickListener {
            bitmap = Bitmaps.crop(bitmap, 500, 800)
            log("bitmap ${bitmap.width} * ${bitmap.height}")
            ivPic.setImageBitmap(bitmap)
        }

        btnCircle.setOnClickListener {
            bitmap = Bitmaps.cropCircle(bitmap, 400)
            log("bitmap ${bitmap.width} * ${bitmap.height}")
            ivPic.setImageBitmap(bitmap)
        }
    }

}