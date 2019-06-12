package com.cs.app.window

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Point
import android.view.Gravity
import android.view.WindowManager
import com.cs.app.log
import com.cs.app.window.view.FloatWindowView


/**
 *
 * author : ChenSen
 * data : 2019/6/6
 * desc:
 */
object FloatWindowManager {

    var isShowing = false

    private var view: FloatWindowView? = null
    private var smallViewParam: WindowManager.LayoutParams? = null
    private var windowManager: WindowManager? = null


    @SuppressLint("ClickableViewAccessibility")
    fun createWindowView(context: Context) {

        if (windowManager == null) {
            windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }


        if (view == null) {

            val point = Point()
            windowManager?.defaultDisplay?.getSize(point)
            val screenWidth = point.x
            val screenHeight = point.y
            context.log("screenWidth $screenWidth  screenHeight $screenHeight ")

            view = FloatWindowView(context)
            smallViewParam = WindowManager.LayoutParams()

            smallViewParam?.type = WindowManager.LayoutParams.TYPE_PHONE
            smallViewParam?.format = PixelFormat.RGBA_8888
            smallViewParam?.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            smallViewParam?.width = FloatWindowView.viewWidth
            smallViewParam?.height = FloatWindowView.viewHeight
            smallViewParam?.gravity = Gravity.LEFT or Gravity.TOP
            smallViewParam?.x = screenWidth
            smallViewParam?.y = screenHeight / 2
            view?.setParams(smallViewParam!!)
            windowManager?.addView(view, smallViewParam)
        } else {
            view?.setParams(smallViewParam!!)
            windowManager?.addView(view, smallViewParam)

        }
    }


    fun removeSmallWindow(context: Context) {
        if (view != null) {
            windowManager?.removeView(view)
        }
    }

    fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        val height = context.resources.getDimensionPixelSize(resourceId)
        context.log("状态栏高度 $height")
        return height
    }
}