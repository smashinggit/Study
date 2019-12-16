package com.cs.bitmap.view

import android.content.Context
import android.graphics.PointF
import android.util.Log
import android.view.MotionEvent

/**
 *
 * author : ChenSen
 * data : 2019/5/29
 * desc:
 */
class MoveGestureDetector(context: Context, var mListener: OnMoveGestureListener) : BaseGestureDetector(context) {

    private lateinit var mCurrentPointer: PointF
    private lateinit var mPrePointer: PointF

    //仅仅为了减少创建内存
    private val mDeltaPointer = PointF()

    //用于记录最终结果，并返回
    private val mExtenalPointer = PointF()


    override fun handleInProgressEvent(event: MotionEvent) {

        val actionCode = event.action and MotionEvent.ACTION_MASK

        when (actionCode) {

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                mListener.onMoveEnd(this)
                resetState()
            }

            MotionEvent.ACTION_MOVE -> {
                updateStateByEvent(event)
                val update = mListener.onMove(this)
                if (update) {
                    mPreMotionEvent?.recycle()
                    mPreMotionEvent = MotionEvent.obtain(event)
                }
            }
        }
    }

    override fun handleStartProgressEvent(event: MotionEvent) {

        val actionCode = event.action and MotionEvent.ACTION_MASK

        when (actionCode) {

            MotionEvent.ACTION_DOWN -> {
                resetState() //防止没有接收到CANCEL or UP ,保险起见
                mPreMotionEvent = MotionEvent.obtain(event)
                updateStateByEvent(event)
            }

            MotionEvent.ACTION_MOVE -> {
                mGestureInProgress = mListener.onMoveBegin(this)
            }
        }
    }

    override fun updateStateByEvent(event: MotionEvent) {

        val prev = mPreMotionEvent
        mPrePointer = calculateFocalPointer(prev!!)
        mCurrentPointer = calculateFocalPointer(event)

        Log.e("tag", mPrePointer.toString() + " ,  " + mCurrentPointer.toString())

        val skipThisMoveEvent = prev.pointerCount != event.pointerCount

        Log.e("tag", "mSkipThisMoveEvent = $skipThisMoveEvent ")

        mExtenalPointer.x = if (skipThisMoveEvent) 0f else mCurrentPointer.x - mPrePointer.x
        mExtenalPointer.y = if (skipThisMoveEvent) 0f else mCurrentPointer.y - mPrePointer.y
    }


    /**
     * 根据event计算多指中心点
     */
    private fun calculateFocalPointer(event: MotionEvent): PointF {

        val pointerCount = event.pointerCount

        var x = 0f
        var y = 0f

        for (i in 0 until pointerCount) {
            x += event.getX(i)
            y += event.getY(i)
        }

        x /= pointerCount
        y /= pointerCount

        return PointF(x, y)
    }


    fun getMoveX() = mExtenalPointer.x
    fun getMoveY() = mExtenalPointer.y


    interface OnMoveGestureListener {

        fun onMoveBegin(detector: MoveGestureDetector): Boolean
        fun onMove(detector: MoveGestureDetector): Boolean
        fun onMoveEnd(detector: MoveGestureDetector)
    }


    open class SimpleMoveGestureDetector : OnMoveGestureListener {
        override fun onMoveBegin(detector: MoveGestureDetector): Boolean {
            return true
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {
        }

    }

}