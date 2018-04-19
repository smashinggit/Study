package com.test.viewdemo.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 *
 * author : ChenSen
 * data : 2018/4/4
 * desc:
 */
class GestureDetectorView : View {
    lateinit var mDetector: GestureDetector


    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }


    private fun init(context: Context) {
        mDetector = GestureDetector(context,object : GestureDetector.SimpleOnGestureListener() {

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                context.log("onDoubleTap")
                return false
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                context.log("onSingleTapUp")
                return false
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mDetector.onTouchEvent(event)
        return true
    }

    fun Context.log(msg: String) {
        Log.d("tag", msg)
    }

}