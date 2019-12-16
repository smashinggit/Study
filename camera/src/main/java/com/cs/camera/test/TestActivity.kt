package com.cs.camera.test

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View

/**
 *
 * author : ChenSen
 * data : 2018/11/2
 * desc:
 */
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(AnimateView(this))
        setContentView(DemoSurfaceView(this))
    }


    internal class AnimateView(context: Context?) : View(context) {

        var radius = 10f
        var mPaint: Paint = Paint()

        init {
            mPaint.color = Color.RED
            mPaint.style = Paint.Style.FILL
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            canvas.translate(300f, 300f)
            canvas.drawCircle(0f, 0f, radius++, mPaint)

            if (radius > 100)
                radius = 10f

            postInvalidate()
        }
    }


}