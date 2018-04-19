package com.test.viewdemo.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.widget.ScrollView

/**
 *
 * author : ChenSen
 * data : 2018/4/4
 * desc:
 */
class VelocityScrollView : ScrollView {
    var mVelocityTracker: VelocityTracker? = null


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {

            MotionEvent.ACTION_DOWN -> mVelocityTracker = VelocityTracker.obtain()

            MotionEvent.ACTION_MOVE -> {
                mVelocityTracker = VelocityTracker.obtain()
                mVelocityTracker?.addMovement(ev)
                mVelocityTracker?.computeCurrentVelocity(100)
                val xVelocity = mVelocityTracker?.xVelocity
                val yVelocity = mVelocityTracker?.yVelocity

                context.log("100毫秒内速度：x $xVelocity    y $yVelocity")
            }

            MotionEvent.ACTION_UP -> {
                mVelocityTracker?.clear()
                mVelocityTracker?.recycle()
            }
        }

        return super.onTouchEvent(ev)
    }

    fun Context.log(msg: String) {
        Log.d("tag", msg)
    }
}