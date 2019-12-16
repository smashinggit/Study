package com.cs.customize.view.ExpandableListView

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE
import android.widget.ExpandableListView

/**
 *
 * author : ChenSen
 * data : 2018/4/17
 * desc:
 */
class PinnedHeaderExpandableListView : ExpandableListView {


    private var mHeaderView: View? = null
    private var mHeaderWidth = 0
    private var mHeaderHeight = 0

    private var mScrollListener: OnScrollListener? = null
    private var mHeaderUpdateListener: OnHeaderUpdateListener? = null

    private var mActionDownHappened = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor (context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor (context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }


    private fun init() {
        // setFadingEdgeLength(0)

        mScrollListener = object : OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (totalItemCount > 0)
                    refreshHeader()

                mScrollListener?.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount)
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                if (mHeaderView != null && scrollState == SCROLL_STATE_IDLE)
                    if (firstVisiblePosition == 0)
                        mHeaderView?.layout(0, 0, mHeaderWidth, mHeaderHeight)

                mScrollListener?.onScrollStateChanged(view, scrollState)

            }
        }
        setOnScrollListener(mScrollListener)
    }

    private fun refreshHeader() {
        mHeaderView?.let {
            var pos = firstVisiblePosition + 1
            var firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePosition))
            var group = getPackedPositionGroup(getExpandableListPosition(pos))

            if (group == firstVisibleGroupPos + 1) {
                var view = getChildAt(1)
                if (view.top <= mHeaderHeight) {
                    var delt = mHeaderHeight - view.top
                    it.layout(0, -delt, mHeaderWidth, mHeaderHeight - delt)
                }
            } else {
                it.layout(0, 0, mHeaderWidth, mHeaderHeight)
            }
            mHeaderUpdateListener?.updatePinnedHeader(firstVisibleGroupPos)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mHeaderView?.let {
            measureChild(it, widthMeasureSpec, heightMeasureSpec)
            mHeaderWidth = it.measuredWidth
            mHeaderHeight = it.measuredHeight
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        mHeaderView?.let {
            it.layout(0, 0, mHeaderWidth, mHeaderHeight)
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        mHeaderView?.let {
            drawChild(canvas, mHeaderView, drawingTime)
        }
    }


    public fun setOnHeaderUpdateListener(listener: OnHeaderUpdateListener) {
        mHeaderUpdateListener = listener
        mHeaderView = listener.getPinnedHeader()
        val firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePosition))
        listener.updatePinnedHeader(firstVisibleGroupPos)
        requestLayout()
        postInvalidate()
    }

    interface OnHeaderUpdateListener {
        fun getPinnedHeader(): View
        fun updatePinnedHeader(firstVisibleGroupPos: Int)
    }
}