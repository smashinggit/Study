package com.test.recyclerviewdemo.common

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.test.recyclerviewdemo.view.RefreshRecyclerView

/**
 *
 * author : ChenSen
 * data : 2018/4/24
 * desc:
 */
interface RefreshViewCreator {

    fun getRefreshView(context: Context, parent: ViewGroup): View

    /**
     * 正在下拉
     * @param currentDragHeight   当前拖动的高度
     * @param refreshViewHeight  总的刷新高度
     * @param currentRefreshStatus 当前状态
     */
    fun onPull(currentDragHeight: Int, refreshViewHeight: Int, currentRefreshStatus: Int, recyclerView: RefreshRecyclerView)

    fun onRefreshing(recyclerView: RefreshRecyclerView)

    fun onRefreshComplete(recyclerView: RefreshRecyclerView)

}