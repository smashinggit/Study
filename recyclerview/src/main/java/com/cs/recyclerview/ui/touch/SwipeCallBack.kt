package com.cs.recyclerview.ui.touch

import android.graphics.Canvas
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.cs.recyclerview.R
import com.cs.recyclerview.adapter.SwipeAdapter
import kotlin.math.abs

/**
 * @Author : ChenSen
 * @Date : 2019/9/26 15:42
 *
 * @Desc : 滑动删除
 */
class SwipeCallBack(private val adapter: SwipeAdapter) : ItemTouchHelper.Callback() {


    /**
     * 设置滑动类型标记
     *  返回一个整数类型的标识，用于判断Item那种移动行为是允许的
     */
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {

        return makeMovementFlags(0, ItemTouchHelper.START or ItemTouchHelper.END)
    }

    /**
     * 拖拽切换Item的回调
     *
     * 如果Item切换了位置，返回true；反之，返回false
     */
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        Log.e("tag", "onMove")
        return true
    }

    /**
     * 滑动删除Item
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.delete(viewHolder.adapterPosition)
        Log.e("tag", "onSwiped $direction")

    }

    /**
     * Item是否支持长按拖动
     * 默认是true
     */
    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    /**
     * Item是否支持滑动
     * 默认是true
     */
    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    /**
     * Item被选中时候回调
     *
     * @param actionState
     *          当前Item的状态
     *          ItemTouchHelper.ACTION_STATE_IDLE   闲置状态
     *          ItemTouchHelper.ACTION_STATE_SWIPE  滑动中状态
     *          ItemTouchHelper#ACTION_STATE_DRAG   拖拽中状态
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        //  item被选中的操作
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder?.itemView?.setBackgroundResource(R.color.gray)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    /**
     * 移动过程中绘制Item
     *
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     *          X轴移动的距离
     * @param dY
     *          Y轴移动的距离
     * @param actionState
     *          当前Item的状态
     * @param isCurrentlyActive
     *          如果当前被用户操作为true，反之为false
     */
    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

        //随着滑动的距离，设置Item的透明度
        val x = abs(dX) + 0.5f
        val width = viewHolder.itemView.width
        val alpha = 1f - x / width
        viewHolder.itemView.alpha = alpha

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    /**
     * 移动过程中绘制Item
     *
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     *          X轴移动的距离
     * @param dY
     *          Y轴移动的距离
     * @param actionState
     *          当前Item的状态
     * @param isCurrentlyActive
     *          如果当前被用户操作为true，反之为false
     */
    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }


    /**
     * 用户操作完毕或者动画完毕后会被调用
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        // 操作完毕后恢复颜色
        viewHolder.itemView.setBackgroundResource(R.color.white)
        viewHolder.itemView.alpha = 1.0f
        super.clearView(recyclerView, viewHolder)
    }


}
