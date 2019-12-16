package com.cs.customize.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 *
 * author : ChenSen
 * data : 2018/4/20
 * desc:
 */
class StickyRecyclerView : RecyclerView {

    var mLineHeight = 0f
    var mTitleHeight = 0f
    var mLeftMargin = 0f
    var mTextSize = 0f


    val dividerColor = "#E1E1E1"
    val titleColor = "#666666"
    val titleBg = "#F2F2F2"

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor (context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor (context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        layoutManager = LinearLayoutManager(context, VERTICAL, false)

        mLineHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, context.resources.displayMetrics)
        mTitleHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30f, context.resources.displayMetrics)
        mLeftMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, context.resources.displayMetrics)
        mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, context.resources.displayMetrics)
    }

    @Deprecated(message = "已弃用")
    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
    }

    // 让 adapter 必须继承 StickyAdapter
    fun setAdapter(adapter: StickyAdapter<*>) {
        addItemDecoration(StickyItemDecoration(adapter))
        super.setAdapter(adapter)
    }


    /**
     * 自定义分割线,通过分割线绘制title
     */
    inner class StickyItemDecoration(@NonNull private val mAdapter: StickyAdapter<*>) : ItemDecoration() {
        private val mPaint = Paint()

        init {
            mPaint.style = Paint.Style.FILL
            mPaint.textSize = mTextSize
        }

        //计算 item间间隙(是普通分割线 ,还是title)
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
            super.getItemOffsets(outRect, view, parent, state)
            val pos = parent.getChildAdapterPosition(view)

            if (mAdapter.needTitle(pos))
                outRect.top = mTitleHeight.toInt()
            else
                outRect.top = mLineHeight.toInt()
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: State) {
            super.onDraw(c, parent, state)
            val left = parent.left
            val right = parent.measuredWidth - parent.paddingRight

            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                val param = child.layoutParams as RecyclerView.LayoutParams
                val pos = parent.getChildAdapterPosition(child)

                if (mAdapter.needTitle(pos)) {//绘制标题
                    val top = child.top + param.topMargin - mTitleHeight
                    val bottom = top + mTitleHeight
                    mPaint.color = Color.parseColor(titleBg)
                    c.drawRect(left.toFloat(), top, right.toFloat(), bottom - mLineHeight, mPaint)

                    mPaint.color = Color.parseColor(dividerColor)
                    c.drawRect(left.toFloat(), bottom - mLineHeight, right.toFloat(), bottom, mPaint)

                    mPaint.color = Color.parseColor(titleColor)
                    val title = mAdapter.getItemViewTitle(pos)
                    val titleRect = Rect(0, 0, 0, 0)
                    mPaint.getTextBounds(title, 0, title.length, titleRect)
                    c.drawText(mAdapter.getItemViewTitle(pos), left + mLeftMargin, (top + mTitleHeight * 0.5 + titleRect.height() * 0.5).toFloat(), mPaint)
                } else {
                    //绘制分割线(此分割线绘制在item的顶部)
                    val top = child.top + param.topMargin - mLineHeight
                    val bottom = top + mLineHeight
                    mPaint.color = Color.parseColor(dividerColor)
                    c.drawRect(left.toFloat() + mLeftMargin, top, right.toFloat(), bottom, mPaint)
                }
            }
        }

        //上层绘制,绘制顶部悬停title
        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: State) {
            super.onDrawOver(c, parent, state)
            val left = parent.left
            val right = parent.measuredWidth - parent.paddingRight
            val top = parent.top - parent.paddingTop
            val bottom = top + mTitleHeight
            mPaint.color = Color.parseColor(titleBg)
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom, mPaint)

            val pos = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            mPaint.color = Color.parseColor(titleColor)
            val title = mAdapter.getItemViewTitle(pos)
            val titleRect = Rect(0, 0, 0, 0)
            mPaint.getTextBounds(title, 0, title.length, titleRect)
            c.drawText(mAdapter.getItemViewTitle(pos), left + mLeftMargin, (top + mTitleHeight * 0.5 + titleRect.height() * 0.5).toFloat(), mPaint)
        }

    }

    /**
     *自定义Adapter
     */
    abstract class StickyAdapter<T : ViewHolder> : RecyclerView.Adapter<T>() {

        // 获取当前 item 的标题
        abstract fun getItemViewTitle(position: Int): String

        // 如果标题和前面的item的标题一样,就不需要绘制
        fun needTitle(position: Int): Boolean {
            return position > -1 && (position == 0 || getItemViewTitle(position) != getItemViewTitle(position - 1))
        }
    }

}