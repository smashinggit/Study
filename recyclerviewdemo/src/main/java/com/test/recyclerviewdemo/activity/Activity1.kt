package com.test.recyclerviewdemo.activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.test.recyclerviewdemo.R
import kotlinx.android.synthetic.main.activity1.*

/**
 *
 * author : ChenSen
 * data : 2018/4/23
 * desc:
 */
class Activity1 : AppCompatActivity() {

    var mLastVisiablePos = 0

    val dataList = ArrayList<String>()
    val adapter = MyAdapter(this, dataList)
    val linearManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


    var mLoadMoreCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity1)

        init()
    }

    private fun init() {

        swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.colorAccent))
        swipeRefreshLayout.isRefreshing = true

        recyclerView.adapter = adapter
        recyclerView.layoutManager = linearManager
        recyclerView.itemAnimator = DefaultItemAnimator()

        loadData()


        swipeRefreshLayout.setOnRefreshListener {
            dataList.clear()
            loadData()
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mLastVisiablePos = linearManager.findLastVisibleItemPosition()

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.d("tag", "newState $newState")

                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisiablePos == (adapter.itemCount - 1)) {
                    loadMore()
                }
            }
        })

    }

    private fun loadData() {
        delay({
            for (i in 0..10) {
                dataList.add("item $i")
            }
            adapter.setData(dataList)
            swipeRefreshLayout.isRefreshing = false
        }, 2000)
    }


    private fun loadMore() {

        if (mLoadMoreCount == 2) {



            return
        }

        delay({
            for (i in 0..4) {
                dataList.add("more item")
            }
            adapter.setData(dataList)
            swipeRefreshLayout.isRefreshing = false
        }, 2000)
        mLoadMoreCount++

    }


    class MyAdapter(private val mContext: Context, private var mData: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        companion object {
            val TYPE_ITEM = 0
            val TYPE_HEADER = 1
            val TYPE_FOOTER = 2
            val TYPE_REFRESH_FOOTER = 3
        }


        fun setData(data: ArrayList<String>) {
            mData = data
            notifyDataSetChanged()
        }


        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is ItemHolder) {
                holder.tvText.text = mData[position - 1]
            }
        }

        override fun getItemCount(): Int = if (mData.size == 0) 0 else mData.size + 3

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

            return when (viewType) {
                TYPE_HEADER -> {
                    val headerView = LayoutInflater.from(mContext).inflate(R.layout.header_recyclerview, parent, false)
                    HeaderHolder(headerView)
                }
                TYPE_FOOTER -> {
                    val footerView = LayoutInflater.from(mContext).inflate(R.layout.footer_recyclerview, parent, false)
                    FooterHolder(footerView)
                }
                TYPE_REFRESH_FOOTER -> {
                    val refreshFooterView = LayoutInflater.from(mContext).inflate(R.layout.default_refresh_footer, parent, false)
                    FooterHolder(refreshFooterView)
                }
                else -> {
                    val itemView = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview, parent, false)
                    ItemHolder(itemView)
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when (position) {
                0 -> TYPE_HEADER
                itemCount - 2 -> TYPE_FOOTER
                itemCount - 1 -> TYPE_REFRESH_FOOTER
                else -> TYPE_ITEM
            }
        }


        class HeaderHolder(var mItemView: View) : RecyclerView.ViewHolder(mItemView)

        class FooterHolder(var mItemView: View) : RecyclerView.ViewHolder(mItemView)

        class RefreshFooterHolder(var mItemView: View) : RecyclerView.ViewHolder(mItemView)

        class ItemHolder(var mItemView: View) : RecyclerView.ViewHolder(mItemView) {
            val tvText = mItemView.findViewById<TextView>(R.id.tvText)
        }
    }


    fun delay(action: () -> Unit, delayTime: Long) {
        Handler().postDelayed(action, delayTime)
    }
}