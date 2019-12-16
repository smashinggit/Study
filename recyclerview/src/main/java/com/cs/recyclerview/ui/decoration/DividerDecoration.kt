package com.cs.recyclerview.ui.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt


/**
 * @Author : ChenSen
 * @Date : 2019/9/27 14:35
 * @Desc :
 */
class DividerDecoration(context: Context, orientation: Int) : RecyclerView.ItemDecoration() {

    private val ATTRS = intArrayOf(android.R.attr.listDivider)

    private val mPaint: Paint = Paint(1)
    private var mDivider: Drawable? = null
    private var mDividerHeight: Int = 0
    private var mOrientation: Int = 0

    init {
        require(!(orientation != 1 && orientation != 0)) { "请输入正确的参数！" }

        mDividerHeight = 2
        mOrientation = orientation

        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
    }

    /**
     * 设置线条颜色和高度给分割线
     */
    constructor(context: Context, orientation: Int, dividerHeight: Int, dividerColor: Int) : this(context, orientation) {
        mDividerHeight = dividerHeight
        mPaint.color = dividerColor
        mPaint.style = Paint.Style.FILL
    }

    /**
     * 设置图片资源为分割线
     */
    constructor(context: Context, orientation: Int, drawableId: Int) : this(context, orientation) {
        mDivider = ContextCompat.getDrawable(context, drawableId)
        mDividerHeight = mDivider?.intrinsicHeight ?: 0
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(0, 0, 0, mDividerHeight)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (mOrientation == 1) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin + child.translationY.roundToInt()
            val right = left + (mDivider?.intrinsicHeight ?: 0)

            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(canvas)
        }
        canvas.restore()
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin + child.translationY.roundToInt()
            val bottom = top + (mDivider?.intrinsicHeight ?: 0)

            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(canvas)
        }
        canvas.restore()
    }
}
