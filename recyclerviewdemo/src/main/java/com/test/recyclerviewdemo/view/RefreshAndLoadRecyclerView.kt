package com.test.recyclerviewdemo.view

import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.test.recyclerviewdemo.common.LoadingViewCreator

/**
 *
 * author : ChenSen
 * data : 2018/4/26
 * desc:
 */
class RefreshAndLoadRecyclerView : RefreshRecyclerView {

    companion object {
        val TYPE_AUTO = 0
        val TYPE_PULL = 1
    }

    private val LOADING_STATUS_PULL_UP_REFRESH = 1           //上拉加载
    private val LOADING_STATUS_LOOSEN_REFRESHING = 2         //松开加载状态
    private val LOADING_STATUS_LOADING = 3                    //正在加载

    private var mCurrentLoadingState = LOADING_STATUS_PULL_UP_REFRESH  //当前刷新状态
    private var mCurrentType = TYPE_AUTO

    private var mLoadingCreator: LoadingViewCreator? = null    //上拉加载的辅助类
    private var mLoadingViewHeight: Int = 0    //上拉加载头部的高度
    private var mLoadingView: View? = null     //上拉加载的View
    private var mAnimator: ValueAnimator? = null

    private var mLastVisiablePos = 0   //列表最后一个可见位置
    private var mDragIndex = 0.7f      // 手指拖拽的阻力指数
    private var mDownY = 0  // 手指按下的Y位置

    var mLoadCompleteListener: OnLoadCompleteListener? = null

    constructor(context: Context) : super(context)
    constructor (context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor (context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                mLastVisiablePos = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (mCurrentType == TYPE_AUTO && newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisiablePos == (adapter.itemCount - 1)) {
                    mLoadingCreator?.onLoading(this@RefreshAndLoadRecyclerView)
                    mCurrentLoadingState = LOADING_STATUS_LOADING
                }
            }
        })
    }

    /**
     * 设置加载类型
     * 自动加载  or 手动加载
     */
    fun setLoadType(type: Int) {
        if (type != TYPE_AUTO && type != TYPE_PULL)
            return

        this.mCurrentType = type
    }

    fun addLoadingViewCreator(loadingViewCreator: LoadingViewCreator) {
        this.mLoadingCreator = loadingViewCreator
        addLoadingView()
    }

    private fun addLoadingView() {
        if (adapter != null && mLoadingCreator != null) {
            val loadingView = mLoadingCreator?.getLoadView(context, this)
            loadingView?.let {
                this.mLoadingView = it
                addFooterView(it)
            }
        }
    }

    override fun dispatchTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                mAnimator?.let {
                    if (it.isRunning || mCurrentLoadingState == LOADING_STATUS_LOADING) {
                        return false
                    }
                }
                mDownY = e.rawY.toInt()
            }

            MotionEvent.ACTION_UP -> {
                if (mCurrentLoadingState == LOADING_STATUS_LOOSEN_REFRESHING) {     //松开加载状态，松手后开始加载
                    smoothScrollTo(0)
                    mLoadingCreator?.onLoading(this)
                    // mStateChangeListener?.onStateChange(mCurrentRefreshState)
                }
//                else if (mCurrentLoadingState == REFRESH_STATUS_PULL_DOWN_REFRESH) { //下拉刷新状态，松手后隐藏
//                    smoothScrollTo(-mRefreshViewHeight)
//                    mCurrentRefreshState = REFRESH_STATUS_NORMAL
//                   // mStateChangeListener?.onStateChange(mCurrentRefreshState)
//                }
                mDownY = e.rawY.toInt()
            }
        }
        return super.dispatchTouchEvent(e)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {

                if (canScrollUp()) {   //如果可以向上滑动，不做处理
                    return super.onTouchEvent(e)
                } else {
                    val dy = (e.rawY - mDownY) * mDragIndex
                    if (dy < 0) {
                        setLoadingViewMarginBottom((-dy).toInt())
                        mLoadingCreator?.onPull(((-mLoadingViewHeight + dy)).toInt(), mLoadingViewHeight, mCurrentLoadingState, this)
                    }
                }
            }
        }
        return super.onTouchEvent(e)
    }


    private fun setLoadingViewMarginBottom(marginBottom: Int) {
        mLoadingView?.let {
            val param = it.layoutParams as MarginLayoutParams
            param.bottomMargin = marginBottom
            updateRefreshState(marginBottom)
            it.layoutParams = param
            log("marginBottom $marginBottom")
        }
    }

    /**
     * 刷新头部滑动到指定位置
     */
    private fun smoothScrollTo(topMargin: Int) {
        mLoadingView?.let {
            val currentTopMargin = (it.layoutParams as MarginLayoutParams).topMargin

            mAnimator = ValueAnimator.ofInt(currentTopMargin, topMargin)
            mAnimator?.duration = 250
            mAnimator?.addUpdateListener {
                val currentValue = it.animatedValue as Int
                setLoadingViewMarginBottom(currentValue)
            }
            mAnimator?.start()
        }
    }

    //更新刷新状态
    private fun updateRefreshState(marginBottom: Int) {
        if (marginBottom > mLoadingViewHeight / 2 && mCurrentLoadingState == LOADING_STATUS_PULL_UP_REFRESH) {
            mCurrentLoadingState = LOADING_STATUS_LOOSEN_REFRESHING
            // mStateChangeListener?.onStateChange(mCurrentRefreshState)
        } else if (marginBottom > 0 && mCurrentLoadingState == LOADING_STATUS_LOOSEN_REFRESHING) {
            mCurrentLoadingState = LOADING_STATUS_PULL_UP_REFRESH
        } else if (marginBottom == 0 && mCurrentLoadingState == LOADING_STATUS_LOOSEN_REFRESHING) {
            mCurrentLoadingState = LOADING_STATUS_PULL_UP_REFRESH
        }
        log("mCurrentLoadingState  $mCurrentLoadingState")
        //  else if (marginBottom)
//        else if ((marginBottom > -mRefreshViewHeight && mCurrentRefreshState == REFRESH_STATUS_NORMAL)
//                || (marginBottom <= 0 && mCurrentRefreshState == REFRESH_STATUS_LOOSEN_REFRESHING)) {
//            mCurrentRefreshState = REFRESH_STATUS_PULL_DOWN_REFRESH
//            mStateChangeListener?.onStateChange(mCurrentRefreshState)
//        } else if (marginBottom > 0 && mCurrentRefreshState == REFRESH_STATUS_PULL_DOWN_REFRESH) {
//            mCurrentRefreshState = REFRESH_STATUS_LOOSEN_REFRESHING
//            mStateChangeListener?.onStateChange(mCurrentRefreshState)
//        }
    }

    /**
     *  判断能否向上滚动  (根据canScrollVertically()源码重写，减去刷新头部的高度)
     *
     *  RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
     *  RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
     */
    private fun canScrollUp(): Boolean {
        val offset = computeVerticalScrollOffset()
        val range = computeVerticalScrollRange() - computeVerticalScrollExtent()
        if (range == 0) return false
        return offset < range - 1
    }

    fun onLoadComplete(delay: Long = 0) {
        // Handler().postDelayed({ smoothScrollTo(-mRefreshViewHeight) }, delay)

        mLoadingCreator?.onLoadingComplete(this)
        mLoadCompleteListener?.onComplete()
        mCurrentLoadingState = LOADING_STATUS_PULL_UP_REFRESH
        //   mStateChangeListener?.onStateChange(mCurrentRefreshState)
//        mRefreshCreator?.onRefreshComplete(this)
//        mCompleteListener?.onComplete()
    }

//
//    interface OnStateChangeListener {
//        fun onStateChange(state: Int)
//    }

    interface OnLoadCompleteListener {
        fun onComplete()
    }

//    fun setOnStateChangeListener(listener: OnStateChangeListener) {
//        this.mStateChangeListener = listener
//    }

    fun setOnLoadCompleteListener(listener: OnLoadCompleteListener) {
        this.mLoadCompleteListener = listener
    }


}