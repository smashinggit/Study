package com.test.recyclerviewdemo.common

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.test.recyclerviewdemo.R
import com.test.recyclerviewdemo.view.RefreshRecyclerView

/**
 *
 * author : ChenSen
 * data : 2018/4/24
 * desc:
 */
class DefaultRefreshCreator : RefreshViewCreator {
    private var tvTip: TextView? = null

    override fun getRefreshView(context: Context, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.default_refresh_header, parent, false)
        tvTip = view.findViewById(R.id.tvTip)
        return view
    }

    override fun onPull(currentDragHeight: Int, refreshViewHeight: Int, currentRefreshStatus: Int, recyclerView: RefreshRecyclerView) {
        if (currentDragHeight >= 0) {
            tvTip?.text = "松开刷新"
        } else
            tvTip?.text = "下拉刷新"
    }

    override fun onRefreshing(recyclerView: RefreshRecyclerView) {
        tvTip?.text = "刷新中..."

        Handler().postDelayed({ recyclerView.onRefreshComplete(1000) }, 1500)
    }

    override fun onRefreshComplete(recyclerView: RefreshRecyclerView) {
        tvTip?.text = "刷新完成"
    }
}