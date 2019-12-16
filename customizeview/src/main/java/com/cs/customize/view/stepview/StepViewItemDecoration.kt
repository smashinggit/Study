package com.cs.customize.view.stepview

import android.content.Context
import android.graphics.*
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import android.view.View

/**
 *
 * author : ChenSen
 * data : 2018/4/18
 * desc:
 */
class StepViewItemDecoration(mContext: Context) : RecyclerView.ItemDecoration() {
    var mPaintGreen = Paint()
    var mPaintGray = Paint()

    // 赋值ItemView的左偏移长度
    var itemLeft = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40f, mContext.resources.displayMetrics)
    // 赋值ItemView的顶部偏移长度
    var itemTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, mContext.resources.displayMetrics)
    // 赋值轴点圆的半径为10
    var bigCircleRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6f, mContext.resources.displayMetrics)
    var smallCircleRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, mContext.resources.displayMetrics)

    init {
        mPaintGreen.color = Color.parseColor("#2ee648")
        mPaintGreen.style = Paint.Style.FILL

        mPaintGray.color = Color.parseColor("#D7D7D7")
        mPaintGray.style = Paint.Style.FILL
        mPaintGray.strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, mContext.resources.displayMetrics)
    }

    //同样是绘制内容，但与onDraw（）的区别是：绘制在图层的最上层
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    }

    //在子视图上设置绘制范围，并绘制内容
    //绘制图层在ItemView以下，所以如果绘制区域与ItemView区域相重叠，会被遮挡
    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            // 轴点 = 圆 = 圆心(x,y)
            val centerX = child.left / 2
            val centerY = child.top + itemTop
            if (i == 0) {
                mPaintGreen.alpha = 128
                canvas.drawCircle(centerX.toFloat(), centerY, (bigCircleRadius * 1.5).toFloat(), mPaintGreen)
                mPaintGreen.alpha = 255
                canvas.drawCircle(centerX.toFloat(), centerY, bigCircleRadius, mPaintGreen)
            } else {
                canvas.drawCircle(centerX.toFloat(), centerY, smallCircleRadius, mPaintGray)
            }


            if (i == 0) {
                val pointCenter = Point(centerX, (centerY + (bigCircleRadius * 1.5)).toInt())
                val pointBottom = Point(centerX, child.bottom)

                canvas.drawLine(pointCenter.x.toFloat(), pointCenter.y.toFloat(), pointBottom.x.toFloat(), pointBottom.y.toFloat(), mPaintGray)

            } else if (i == childCount - 1) {
                val pointTop = Point(centerX, (child.top - itemTop).toInt())
                val pointCenter = Point(centerX, (centerY - smallCircleRadius).toInt())
                canvas.drawLine(pointTop.x.toFloat(), pointTop.y.toFloat(), pointCenter.x.toFloat(), pointCenter.y.toFloat(), mPaintGray)
            } else {
                val pointTop = Point(centerX, (child.top - itemTop).toInt())
                val pointCenter1 = Point(centerX, (centerY - smallCircleRadius).toInt())
                val pointCenter2 = Point(centerX, (centerY + smallCircleRadius).toInt())
                val pointBottom = Point(centerX, child.bottom)

                canvas.drawLine(pointTop.x.toFloat(), pointTop.y.toFloat(), pointCenter1.x.toFloat(), pointCenter1.y.toFloat(), mPaintGray)
                canvas.drawLine(pointBottom.x.toFloat(), pointBottom.y.toFloat(), pointCenter2.x.toFloat(), pointCenter2.y.toFloat(), mPaintGray)
            }


        }
    }

    //设置ItemView的内嵌偏移长度（inset）
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.set(itemLeft.toInt(), itemTop.toInt(), 0, 0)
    }
}