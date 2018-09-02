package com.test.recyclerviewdemo.common

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.test.recyclerviewdemo.view.RefreshAndLoadRecyclerView
import com.test.recyclerviewdemo.view.RefreshRecyclerView

/**
 *
 * author : ChenSen
 * data : 2018/4/26
 * desc:
 */
interface LoadingViewCreator {

    fun getLoadView(context: Context, parent: ViewGroup): View

    fun onPull(currentDragHeight: Int, refreshViewHeight: Int, currentRefreshStatus: Int, recyclerView: RefreshRecyclerView)

    fun onLoading(recyclerView: RefreshAndLoadRecyclerView)

    fun onLoadingComplete(recyclerView: RefreshAndLoadRecyclerView)


}