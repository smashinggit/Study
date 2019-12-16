package com.cs.camera.test

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 *
 * author : ChenSen
 * data : 2018/11/2
 * desc:
 *
 * 1. SurfaceView允许其他线程更新视图对象(执行绘制方法)而View不允许这么做，它只允许UI线程更新视图对象。

2. SurfaceView是放在其他最底层的视图层次中，所有其他视图层都在它上面，所以在它之上可以添加一些层，而且它不能是透明的。

3. 它执行动画的效率比View高，而且你可以控制帧数。
 */
class DemoSurfaceView : SurfaceView, SurfaceHolder.Callback {

    lateinit var thread: LoopThread

    constructor(context: Context) : super(context) {
        init(context)
    }

    private fun init(context: Context) {
        holder.addCallback(this)

        thread = LoopThread(context, holder)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        thread.isRunning = false
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        thread.isRunning = true
        thread.start()
    }

    class LoopThread(val context: Context, val holder: SurfaceHolder) : Thread() {
        var isRunning = false
        var radius = 10f
        var paint = Paint()

        init {
            paint.style = Paint.Style.FILL
            paint.color = Color.RED
        }

        override fun run() {
            var canvas: Canvas? = null

            while (isRunning) {

                try {
                    synchronized(holder) {

                        canvas = holder.lockCanvas()
                        doDraw(canvas)
                        Thread.sleep(50)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }

        private fun doDraw(canvas: Canvas?) {
            //这个很重要，清屏操作，清楚掉上次绘制的残留图像
            canvas?.let {
                it.drawColor(Color.WHITE)

                it.translate(300f, 300f)
                it.drawCircle(0f, 0f, radius++, paint)
                if (radius > 100)
                    radius = 10f
            }
        }
    }


}