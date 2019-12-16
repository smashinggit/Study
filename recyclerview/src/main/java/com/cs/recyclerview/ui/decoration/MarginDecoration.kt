package com.cs.recyclerview.ui.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @Author : ChenSen
 * @Date : 2019/9/26 11:13
 *
 * @Desc :
 */
class MarginDecoration(private val left: Int = 0,
                       private val top: Int = 0,
                       private val right: Int = 0,
                       private val bottom: Int = 0) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = left
        outRect.top = top
        outRect.right = right
        outRect.bottom = bottom

    }
}