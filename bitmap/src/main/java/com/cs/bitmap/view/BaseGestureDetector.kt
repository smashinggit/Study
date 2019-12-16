package com.cs.bitmap.view

import android.content.Context
import android.view.MotionEvent

/**
 *
 * author : ChenSen
 * data : 2019/5/29
 * desc:
 */
abstract class BaseGestureDetector(protected val mContext: Context) {


    protected var mGestureInProgress = false
    protected var mPreMotionEvent: MotionEvent? = null
    protected var mCurrentMotionEvent: MotionEvent? = null


    public fun onTouchEvent(event: MotionEvent): Boolean {

        if (!mGestureInProgress) {
            handleStartProgressEvent(event)
        } else {
            handleInProgressEvent(event)
        }
        return true
    }

    protected fun resetState() {

        if (mPreMotionEvent != null) {
            mPreMotionEvent?.recycle()
            mPreMotionEvent = null
        }

        if (mCurrentMotionEvent != null) {
            mCurrentMotionEvent?.recycle()
            mCurrentMotionEvent = null
        }
        mGestureInProgress = false
    }


    abstract fun handleInProgressEvent(event: MotionEvent)

    abstract fun handleStartProgressEvent(event: MotionEvent)

    abstract fun updateStateByEvent(event: MotionEvent)

}