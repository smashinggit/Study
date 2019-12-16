package com.cs.customize.view.stepview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 *
 * author : ChenSen
 * data : 2018/4/18
 * desc:
 */
class MyItemDecoration : RecyclerView.ItemDecoration() {
    var mPaint = Paint()

    init {
        mPaint.color = Color.YELLOW
        mPaint.style = Paint.Style.FILL

    }

    //同样是绘制内容，但与onDraw（）的区别是：绘制在图层的最上层
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    }

    //在子视图上设置绘制范围，并绘制内容
    //绘制图层在ItemView以下，所以如果绘制区域与ItemView区域相重叠，会被遮挡
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)


        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams

            val left = parent.left + parent.paddingLeft      // ItemView的左边界 = RecyclerView 的左边界 + paddingLeft距离 后的位置
            val right = parent.width - parent.paddingRight   // ItemView的右边界 = RecyclerView 的右边界减去 paddingRight 后的坐标位置
            val top = child.bottom + layoutParams.bottomMargin     // ItemView的下边界：ItemView 的 bottom坐标 + 距离RecyclerView底部距离 +translationY
            val bottom = child.bottom + 10                         // 绘制分割线的下边界 = ItemView的下边界+分割线的高度

            val rect = Rect(left, top, right, bottom)
            c.drawRect(rect, mPaint)
        }

    }

    //设置ItemView的内嵌偏移长度（inset）
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val pos = parent.getChildAdapterPosition(view)
        // 第1个Item不绘制分割线
        outRect.set(0, 0, 0, 10)
    }
}