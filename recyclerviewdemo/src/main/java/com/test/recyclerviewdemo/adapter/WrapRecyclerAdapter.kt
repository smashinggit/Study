package com.test.recyclerviewdemo.adapter

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup

/**
 *
 * author : ChenSen
 * data : 2018/4/23
 * desc:
 */
class WrapRecyclerAdapter(var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var TYPE_HEADER = 1000000   //头部类型
    var TYPE_FOOTER = 2000000   //底部类型

    var mHeadersView: SparseArray<View> = SparseArray()
    var mFootersView: SparseArray<View> = SparseArray()


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

        if (isHeaderViewType(viewType)) {
            val headerView = mHeadersView.get(viewType)
//            if (headerView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
//                (headerView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
//            }
            return createHeaderAndFooterViewHolder(headerView)
        }

        if (isFooterViewType(viewType)) {
            val footerView = mFootersView.get(viewType)
//            if (footerView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
//                (footerView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
//            }
            return createHeaderAndFooterViewHolder(footerView)
        }

        return mAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (isHeaderPosition(position) || isFooterPosition(position))
            return

        val realPosition = position - mHeadersView.size()
        mAdapter.onBindViewHolder(holder, realPosition)
    }

    override fun getItemCount(): Int = mAdapter.itemCount + mHeadersView.size() + mFootersView.size()

    override fun getItemViewType(position: Int): Int {
        if (isHeaderPosition(position))
            return mHeadersView.keyAt(position)

        if (isFooterPosition(position)) {
            val realPos = position - mHeadersView.size() - mAdapter.itemCount
            return mFootersView.keyAt(realPos)
        }

        val realPos = position - mHeadersView.size()
        return mAdapter.getItemViewType(realPos)
    }

    fun isHeaderViewType(viewType: Int): Boolean {
        val index = mHeadersView.indexOfKey(viewType)
        return index >= 0
    }

    fun isFooterViewType(viewType: Int): Boolean {
        val index = mFootersView.indexOfKey(viewType)
        return index >= 0
    }


    fun isHeaderPosition(position: Int): Boolean {
        return position < mHeadersView.size()
    }

    fun isFooterPosition(position: Int): Boolean {
        return position >= mHeadersView.size() + mAdapter.itemCount
    }


    //创建头部或者底部的ViewHolder
    private fun createHeaderAndFooterViewHolder(view: View): RecyclerView.ViewHolder {
        return WrapViewHolder(view)
    }

    class WrapViewHolder(view: View) : RecyclerView.ViewHolder(view)


    fun addHeaderView(view: View) {
        val pos = mHeadersView.indexOfValue(view)
        if (pos < 0)
            mHeadersView.put(TYPE_HEADER++, view)

        notifyDataSetChanged()
    }

    fun addFooterView(view: View) {
        val pos = mFootersView.indexOfValue(view)
        if (pos < 0)
            mFootersView.put(TYPE_FOOTER++, view)

        notifyDataSetChanged()
    }


    fun removeHeaderView(view: View) {
        val pos = mHeadersView.indexOfValue(view)
        if (pos < 0)
            return
        mHeadersView.remove(pos)
        notifyDataSetChanged()
    }

    fun removeFooterView(view: View) {
        val pos = mFootersView.indexOfValue(view)
        if (pos < 0)
            return
        mFootersView.remove(pos)
        notifyDataSetChanged()
    }

    //适配网格布局 头部和底部
    fun adjustSpanSize(recyclerView: RecyclerView) {
        if (recyclerView.layoutManager is GridLayoutManager) {
            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val isHeaderOrFooter = isHeaderPosition(position) || isFooterPosition(position)
                    return if (isHeaderOrFooter) layoutManager.spanCount else 1
                }
            }
        }
    }
}