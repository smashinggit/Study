package com.cs.app.window.view

import android.content.Context
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import com.cs.app.R
import com.cs.app.window.FloatWindowManager
import kotlinx.android.synthetic.main.float_window_small.view.*

/**
 *
 * author : ChenSen
 * data : 2019/6/6
 * desc:
 */
class FloatWindowView(context: Context) : LinearLayout(context) {

    companion object {
        var viewWidth: Int = 0     //小悬浮窗的宽高
        var viewHeight: Int = 0
        var statusBarHeight = 0    //系统状态栏的高度
    }

    private var mWindowManager: WindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private lateinit var mLayoutParams: WindowManager.LayoutParams


    private var currentX = 0f //当前手指位置在屏幕上的坐标
    private var currentY = 0f

    private var downX = 0f //手指按下时在屏幕上的坐标
    private var downY = 0f

    private var xInView = 0f //手指按下时小悬浮窗在屏幕上的坐标
    private var yInView = 0f


    init {
        statusBarHeight = FloatWindowManager.getStatusBarHeight(context)
        LayoutInflater.from(context).inflate(R.layout.float_window_small, this)

        viewWidth = small_window_layout.layoutParams.width
        viewHeight = small_window_layout.layoutParams.height
        tvPercent.text = "悬浮窗"
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {

            MotionEvent.ACTION_DOWN -> {
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.x
                yInView = event.y

                downX = event.rawX
                downY = event.rawY - statusBarHeight

                currentX = event.rawX
                currentY = event.rawY - statusBarHeight

            }
            MotionEvent.ACTION_MOVE -> {
                currentX = event.rawX
                currentY = event.rawY - statusBarHeight

                // 手指移动的时候更新小悬浮窗的位置
                updateViewPosition()
            }
            MotionEvent.ACTION_UP -> {
                // 如果手指离开屏幕时，currentX和downX相等，且currentY 和downY相等，则视为触发了单击事件。
                if (currentX == downX && currentY == downY) {
                    Toast.makeText(context, "点击", 0).show()
                }

            }
            MotionEvent.ACTION_OUTSIDE -> {
//                Toast.makeText(context,"ACTION_OUTSIDE",0).show()
            }
        }

        return true
    }

    /**
     * 更新小悬浮窗的位置
     */
    private fun updateViewPosition() {
        mLayoutParams.x = ((currentX - xInView).toInt())
        mLayoutParams.y = ((currentY - yInView).toInt())
        mWindowManager.updateViewLayout(this, mLayoutParams)
    }


    private fun openBigWindow() {
    }


    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置
     */
    fun setParams(params: WindowManager.LayoutParams) {
        this.mLayoutParams = params
    }


}