package com.test.recyclerviewdemo.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.test.recyclerviewdemo.R
import com.test.recyclerviewdemo.adapter.WrapRecyclerAdapter
import com.test.recyclerviewdemo.common.DefaultLoadingCreator
import com.test.recyclerviewdemo.common.DefaultRefreshCreator
import com.test.recyclerviewdemo.view.RefreshAndLoadRecyclerView
import com.test.recyclerviewdemo.view.RefreshRecyclerView
import kotlinx.android.synthetic.main.activity_xrecyclerview.*

/**
 *
 * author : ChenSen
 * data : 2018/4/24
 * desc:
 */
class XRecyclerViewActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xrecyclerview)

        val dataList = ArrayList<String>()
        (0..9).mapTo(dataList) { "item $it" }

        val mAdapter = MyAdapter(this, dataList)
        val mWrapRecyclerAdapter = WrapRecyclerAdapter(mAdapter)

        xRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        // xRecyclerView.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        // xRecyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        xRecyclerView.adapter = mWrapRecyclerAdapter

        xRecyclerView.addRefreshViewCreator(DefaultRefreshCreator())   //下拉刷新
        xRecyclerView.addLoadingViewCreator(DefaultLoadingCreator())   //上拉加载
        xRecyclerView.setLoadType(RefreshAndLoadRecyclerView.TYPE_PULL)

        val headerView = LayoutInflater.from(this).inflate(R.layout.header_recyclerview, xRecyclerView, false)
        mWrapRecyclerAdapter.addHeaderView(headerView)

        xRecyclerView.setOnCompleteListener(object : RefreshRecyclerView.OnCompleteListener {
            override fun onComplete() {
                log("onComplete")
            }
        })

        xRecyclerView.setOnStateChangeListener(object : RefreshRecyclerView.OnStateChangeListener {
            override fun onStateChange(state: Int) {
                log("onStateChange  $state")
            }
        })

        xRecyclerView.setOnLoadCompleteListener(object : RefreshAndLoadRecyclerView.OnLoadCompleteListener {
            override fun onComplete() {
                for (i in 0..4) {
                    dataList.add("item load more")
                }
                mWrapRecyclerAdapter.notifyDataSetChanged()
            }

        })
    }

    class MyAdapter(private val mContext: Context, val data: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is MyHolder)
                holder.tvText.text = data[position]
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview, parent, false)
            return MyHolder(view)
        }

        override fun getItemCount(): Int = data.size
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvText = itemView.findViewById<TextView>(R.id.tvText)
    }

    fun log(msg: String) {
        Log.d("tag", msg)
    }
}