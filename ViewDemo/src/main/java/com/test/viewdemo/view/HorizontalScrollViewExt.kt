package com.test.viewdemo.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller

/**
 *
 * author : ChenSen
 * data : 2018/4/16
 * desc:
 */
class HorizontalScrollViewExt : ViewGroup {

    private var mChildCount = 0
    private var mChildIndex = 0
    private var mChildWidth = 0
    private var mScreenWidth = 0

    private var mLastX = 0
    private var mLastY = 0

    private var mLastXIntercept = 0
    private var mLastYIntercept = 0

    private lateinit var mScroller: Scroller
    private lateinit var mVelocityTracker: VelocityTracker


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
        mScroller = Scroller(context)
        mVelocityTracker = VelocityTracker.obtain()

        mScreenWidth = context.resources.displayMetrics.widthPixels
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var measureWidth = 0
        var measureHeight = 0

        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        Log.e("tag", "widthMode ${widthMode == MeasureSpec.AT_MOST}   heightMode ${heightMode == MeasureSpec.AT_MOST} ")

        if (childCount == 0)
            setMeasuredDimension(0, 0)
        else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            val childView = getChildAt(0)
            measureWidth = childView.measuredWidth * childCount
            measureHeight = childView.measuredHeight
            setMeasuredDimension(measureWidth, measureHeight)
            Log.e("tag", "111")
        } else if (widthMode == MeasureSpec.AT_MOST) {
            val childView = getChildAt(0)
            measureWidth = childView.measuredWidth * childCount
            setMeasuredDimension(measureWidth, heightSize)
            Log.e("tag", "222")
        } else if (heightMode == MeasureSpec.AT_MOST) {
            val childView = getChildAt(0)
            measureHeight = childView.measuredHeight
            setMeasuredDimension(widthSize, measureHeight)
            Log.e("tag", "333")
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childLeft = 0
        mChildCount = childCount

        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView.visibility != View.GONE) {

                //  val childWidth = childView.measuredWidth
                val childWidth = mScreenWidth
                mChildWidth = childWidth

                childView?.layout(childLeft, 0, childLeft + childWidth, childView.measuredHeight)
                childLeft += childWidth
            }
        }
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var isIntercept = false

        val x = ev.x
        val y = ev.y

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                isIntercept = false
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                    isIntercept = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltX = x - mLastXIntercept
                val deltY = y - mLastYIntercept

                isIntercept = Math.abs(deltX) > Math.abs(deltY) + 20
            }

            MotionEvent.ACTION_UP -> {
                isIntercept = false
            }
        }
        Log.e("tag", "isIntercept   $isIntercept")

        mLastX = x.toInt()
        mLastY = y.toInt()
        mLastXIntercept = x.toInt()
        mLastYIntercept = y.toInt()

        return isIntercept
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mVelocityTracker.addMovement(event)
        val x = event.x
        val y = event.y

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {
                if (!mScroller.isFinished) {
                    mScroller.abortAnimation()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val deltX = x - mLastX
                var deltY = y - mLastY
                scrollBy((-deltX).toInt(), 0)
            }

            MotionEvent.ACTION_UP -> {
                mVelocityTracker.computeCurrentVelocity(1000)
                val xVelocity = mVelocityTracker.xVelocity
                mChildIndex = if (Math.abs(xVelocity) >= 50 && (scrollX > 0 || scaleX < measuredWidth))
                    if (xVelocity > 0) mChildIndex - 1 else mChildIndex + 1
                else
                    (scrollX + mChildWidth / 2) / mChildWidth

                mChildIndex = Math.max(0, Math.min(mChildIndex, mChildCount - 1))
                val dx = mChildIndex * mChildWidth - scrollX
                smoothScrollBy(dx, 0)
                mVelocityTracker.clear()
            }
        }

        mLastX = x.toInt()
        mLastY = y.toInt()
        return true
    }

    private fun smoothScrollBy(dx: Int, i: Int) {
        mScroller.startScroll(scrollX, 0, dx, 0, 500)
        invalidate()
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            postInvalidate()
        }
    }

    override fun onDetachedFromWindow() {
        mVelocityTracker.recycle()
        super.onDetachedFromWindow()
    }
}