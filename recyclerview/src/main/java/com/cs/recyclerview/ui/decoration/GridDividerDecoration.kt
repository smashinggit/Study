package com.cs.recyclerview.ui.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @Author : ChenSen
 * @Date : 2019/9/27 15:38
 * @Desc :
 */
class GridDividerDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val ATTRS = intArrayOf(android.R.attr.listDivider)

    private val mDivider: Drawable?

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val spanCount = getSpanCount(parent)
        val childCount = parent.childCount

        when {
            // 如果是最后一行，则不需要绘制底部
            isLastRaw(parent, spanCount, childCount) -> outRect.set(0, 0, mDivider!!.intrinsicWidth, 0)

            // 如果是最后一列，则不需要绘制右边
            isLastColum(parent, spanCount, view) -> outRect.set(0, 0, 0, mDivider!!.intrinsicHeight)

            else -> outRect.set(0, 0, mDivider!!.intrinsicWidth, mDivider.intrinsicHeight)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawHorizontal(c, parent)
        drawVertical(c, parent)
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val left = child.left - params.leftMargin
            val right = (child.right + params.rightMargin + mDivider!!.intrinsicWidth)
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider.intrinsicHeight

            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.top - params.topMargin
            val bottom = child.bottom + params.bottomMargin
            val left = child.right + params.rightMargin
            val right = left + mDivider!!.intrinsicWidth

            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(c)
        }
    }


    /**
     * 获取列数
     */
    private fun getSpanCount(parent: RecyclerView): Int {
        var spanCount = -1
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            spanCount = layoutManager.spanCount
        }
        return spanCount
    }

    private fun isLastColum(parent: RecyclerView, spanCount: Int, view: View): Boolean {
        val layoutManager = parent.layoutManager
        val pos = layoutManager!!.getPosition(view)
        if (layoutManager is GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                return true
            }
        }
        return false
    }

    private fun isLastRaw(parent: RecyclerView, spanCount: Int,
                          childCount: Int): Boolean {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            if (childCount % spanCount == 0 || childCount % spanCount < spanCount)
                return true
        }
        return false
    }

}
