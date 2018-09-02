package com.test.recyclerviewdemo.common

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.test.recyclerviewdemo.R
import com.test.recyclerviewdemo.view.RefreshAndLoadRecyclerView
import com.test.recyclerviewdemo.view.RefreshRecyclerView

/**
 *
 * author : ChenSen
 * data : 2018/4/24
 * desc:
 */
class DefaultLoadingCreator : LoadingViewCreator {
    private var tvTip: TextView? = null

    override fun getLoadView(context: Context, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.default_refresh_footer, parent, false)
        tvTip = view.findViewById(R.id.tvTip)
        return view
    }

    override fun onPull(currentDragHeight: Int, refreshViewHeight: Int, currentRefreshStatus: Int, recyclerView: RefreshRecyclerView) {
        if (currentDragHeight >= 0) {
            tvTip?.text = "松开加载更多"
        } else
            tvTip?.text = "上拉加载"
    }

    override fun onLoading(recyclerView: RefreshAndLoadRecyclerView) {
        tvTip?.text = "加载中..."

        Handler().postDelayed({ recyclerView.onLoadComplete(1000) }, 1500)
    }

    override fun onLoadingComplete(recyclerView: RefreshAndLoadRecyclerView) {
        tvTip?.text = "加载完成"
    }
}