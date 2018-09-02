package com.test.recyclerviewdemo.view

import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.test.recyclerviewdemo.common.LoadingViewCreator
import com.test.recyclerviewdemo.common.RefreshViewCreator

/**
 *
 * author : ChenSen
 * data : 2018/4/24
 * desc: 下拉刷新的RecyclerView
 */
open class RefreshRecyclerView : WrapRecyclerView {

    private val REFRESH_STATUS_NORMAL = 0                    //默认状态
    private val REFRESH_STATUS_PULL_DOWN_REFRESH = 1         //下拉刷新状态
    private val REFRESH_STATUS_LOOSEN_REFRESHING = 2         //松开刷新状态
    private val REFRESH_STATUS_REFRESHING = 3                //正在刷新状态

    private var mRefreshCreator: RefreshViewCreator? = null    // 下拉刷新的辅助类
    private var mRefreshViewHeight: Int = 0    // 下拉刷新头部的高度
    private var mRefreshView: View? = null   // 下拉刷新的头部View
    private var mAnimator: ValueAnimator? = null

    private var mCurrentRefreshState = REFRESH_STATUS_NORMAL  //当前刷新状态
    private var mDragIndex = 0.7f  // 手指拖拽的阻力指数
    private var mDownY = 0  // 手指按下的Y位置

    private var mStateChangeListener: OnStateChangeListener? = null
    private var mCompleteListener: OnCompleteListener? = null

    constructor(context: Context) : super(context)
    constructor (context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor (context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun addRefreshViewCreator(refreshViewCreator: RefreshViewCreator) {
        this.mRefreshCreator = refreshViewCreator
        addRefreshView()
    }

    /**
     * 加头部刷新的View
     */
    private fun addRefreshView() {
        if (adapter != null && mRefreshCreator != null) {
            val refreshView = mRefreshCreator?.getRefreshView(context, this)
            refreshView?.let {
                this.mRefreshView = it
                addHeaderView(it)
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed && mRefreshView != null && mRefreshViewHeight <= 0) {

            mRefreshView?.let {
                if (it.measuredHeight > 0) {
                    mRefreshViewHeight = it.measuredHeight
                    // 隐藏头部刷新的View
                    setRefreshViewMarginTop(-mRefreshViewHeight)
                }
            }
        }
    }

    override fun dispatchTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                mAnimator?.let {
                    if (it.isRunning || mCurrentRefreshState == REFRESH_STATUS_REFRESHING) {
                        return false
                    }
                }
                mDownY = e.rawY.toInt()
            }

            MotionEvent.ACTION_UP -> {
                if (mCurrentRefreshState == REFRESH_STATUS_LOOSEN_REFRESHING) {     //松开刷新状态，松手后开始刷新
                    smoothScrollTo(0)
                    mCurrentRefreshState = REFRESH_STATUS_REFRESHING
                    mRefreshCreator?.onRefreshing(this)
                    mStateChangeListener?.onStateChange(mCurrentRefreshState)
                } else if (mCurrentRefreshState == REFRESH_STATUS_PULL_DOWN_REFRESH) { //下拉刷新状态，松手后隐藏
                    smoothScrollTo(-mRefreshViewHeight)
                    mCurrentRefreshState = REFRESH_STATUS_NORMAL
                    mStateChangeListener?.onStateChange(mCurrentRefreshState)
                }
                mDownY = e.rawY.toInt()
            }
        }
        return super.dispatchTouchEvent(e)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {

                if (canScrollDown()) {   //如果可以向下滑动，不做处理
                    return super.onTouchEvent(e)
                } else {
                    // 如果是已经到达头部，并且不断的向下拉，那么不断的改变refreshView的marginTop的值
                    val dy = (e.rawY - mDownY) * mDragIndex
                    if (dy > 0) {
                        setRefreshViewMarginTop(((-mRefreshViewHeight + dy)).toInt())
                        mRefreshCreator?.onPull(((-mRefreshViewHeight + dy)).toInt(), mRefreshViewHeight, mCurrentRefreshState, this)
                    }
                }
            }
        }
        return super.onTouchEvent(e)
    }

    //设置刷新View的marginTop
    private fun setRefreshViewMarginTop(marginTop: Int) {
        mRefreshView?.let {
            log("marginTop $marginTop")
            val param = it.layoutParams as MarginLayoutParams
            param.topMargin = marginTop
            updateRefreshState(marginTop)
            it.layoutParams = param
        }
    }

    //更新刷新状态
    private fun updateRefreshState(marginTop: Int) {
        if (marginTop == -mRefreshViewHeight && mCurrentRefreshState != REFRESH_STATUS_NORMAL) {
            mCurrentRefreshState = REFRESH_STATUS_NORMAL
            mStateChangeListener?.onStateChange(mCurrentRefreshState)
        } else if ((marginTop > -mRefreshViewHeight && mCurrentRefreshState == REFRESH_STATUS_NORMAL)
                || (marginTop <= 0 && mCurrentRefreshState == REFRESH_STATUS_LOOSEN_REFRESHING)) {
            mCurrentRefreshState = REFRESH_STATUS_PULL_DOWN_REFRESH
            mStateChangeListener?.onStateChange(mCurrentRefreshState)
        } else if (marginTop > 0 && mCurrentRefreshState == REFRESH_STATUS_PULL_DOWN_REFRESH) {
            mCurrentRefreshState = REFRESH_STATUS_LOOSEN_REFRESHING
            mStateChangeListener?.onStateChange(mCurrentRefreshState)
        }
    }

    //刷新头部滑动到指定位置
    private fun smoothScrollTo(topMargin: Int) {
        mRefreshView?.let {
            val currentTopMargin = (it.layoutParams as MarginLayoutParams).topMargin

            mAnimator = ValueAnimator.ofInt(currentTopMargin, topMargin)
            mAnimator?.duration = 250
            mAnimator?.addUpdateListener {
                val currentValue = it.animatedValue as Int
                setRefreshViewMarginTop(currentValue)
            }
            mAnimator?.start()
        }
    }

    /**
     *  判断能否向下滚动  (根据canScrollVertically()源码重写，减去刷新头部的高度)
     *
     *  RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
     *  RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
     */
    private fun canScrollDown(): Boolean {
        val offset = computeVerticalScrollOffset() - mRefreshViewHeight
        val range = computeVerticalScrollRange() - computeVerticalScrollExtent()
        if (range == 0) return false
        return offset > 0
    }

    fun onRefreshComplete(delay: Long = 0) {
        Handler().postDelayed({ smoothScrollTo(-mRefreshViewHeight) }, delay)

        mCurrentRefreshState = REFRESH_STATUS_NORMAL
        mStateChangeListener?.onStateChange(mCurrentRefreshState)
        mRefreshCreator?.onRefreshComplete(this)
        mCompleteListener?.onComplete()
    }

    fun log(msg: String) {
        Log.d("tag", msg)
    }

    interface OnStateChangeListener {
        fun onStateChange(state: Int)
    }

    interface OnCompleteListener {
        fun onComplete()
    }

    fun setOnStateChangeListener(listener: OnStateChangeListener) {
        this.mStateChangeListener = listener
    }

    fun setOnCompleteListener(listener: OnCompleteListener) {
        this.mCompleteListener = listener
    }

}