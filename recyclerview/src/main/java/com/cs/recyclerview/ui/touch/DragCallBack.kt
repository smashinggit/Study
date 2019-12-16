package com.cs.recyclerview.ui.touch

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.cs.recyclerview.R
import com.cs.recyclerview.adapter.DragAdapter

/**
 * @Author : ChenSen
 * @Date : 2019/9/27 14:53
 *
 * @Desc :
 */
class DragCallBack(private val adapter: DragAdapter) : ItemTouchHelper.Callback() {

    /**
     * 返回一个整数类型的标识，用于判断Item那种移动行为是允许的
     */
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT or ItemTouchHelper.DOWN or ItemTouchHelper.UP, 0)
    }

    /**
     * Item是否支持长按拖动
     *
     * @return
     *          true  支持长按操作
     *          false 不支持长按操作
     */
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    /**
     * Item是否支持滑动
     *
     * @return
     *          true  支持滑动操作
     *          false 不支持滑动操作
     */
    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    /**
     * 拖拽切换Item的回调
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     *          如果Item切换了位置，返回true；反之，返回false
     */
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        adapter.move(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    /**
     * Item被选中时候回调
     *
     * @param viewHolder
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
     * 用户操作完毕或者动画完毕后会被调用
     *
     * @param recyclerView
     * @param viewHolder
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        // 操作完毕后恢复颜色
        viewHolder.itemView.setBackgroundResource(R.drawable.border_gray)
        super.clearView(recyclerView, viewHolder)
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }
}