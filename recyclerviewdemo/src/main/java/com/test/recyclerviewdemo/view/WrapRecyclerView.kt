package com.test.recyclerviewdemo.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.test.recyclerviewdemo.adapter.WrapRecyclerAdapter

/**
 *
 * author : ChenSen
 * data : 2018/4/24
 * desc:
 */
open class WrapRecyclerView : RecyclerView {

    private var mWrapRecyclerAdapter: WrapRecyclerAdapter? = null
    private var mAdapter: Adapter<ViewHolder>? = null

    constructor(context: Context) : super(context)
    constructor (context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor (context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        if (mAdapter != null) {
            mAdapter?.unregisterAdapterDataObserver(mDataObserver)
            mAdapter = null
        }
        this.mAdapter = adapter

        mWrapRecyclerAdapter = if (adapter is WrapRecyclerAdapter)
            adapter
        else
            WrapRecyclerAdapter(mAdapter!!)


        super.setAdapter(mWrapRecyclerAdapter)

        // 注册一个观察者
        mAdapter?.registerAdapterDataObserver(mDataObserver)

        // 解决GridLayout添加头部和底部也要占据一行
        mWrapRecyclerAdapter?.adjustSpanSize(this)

    }


    fun addHeaderView(view: View) {
        // 如果没有Adapter那么就不添加，也可以选择抛异常提示
        // 让他必须先设置Adapter然后才能添加，这里是仿照ListView的处理方式
        mWrapRecyclerAdapter?.addHeaderView(view)
    }


    fun addFooterView(view: View) {
        mWrapRecyclerAdapter?.addFooterView(view)
    }

    fun removeHeaderView(view: View) {
        mWrapRecyclerAdapter?.removeHeaderView(view)
    }

    fun removeFooterView(view: View) {
        mWrapRecyclerAdapter?.removeFooterView(view)
    }


    private var mDataObserver = object : AdapterDataObserver() {

        override fun onChanged() {
            if (adapter == null) return
            if (mWrapRecyclerAdapter != adapter)
                mWrapRecyclerAdapter?.notifyDataSetChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            if (adapter == null) return
            if (mWrapRecyclerAdapter != adapter)
                mWrapRecyclerAdapter?.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            if (adapter == null) return
            if (mWrapRecyclerAdapter != adapter)
                mWrapRecyclerAdapter?.notifyItemRangeRemoved(fromPosition, toPosition)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (adapter == null) return
            if (mWrapRecyclerAdapter != adapter)
                mWrapRecyclerAdapter?.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            if (adapter == null) return
            if (mWrapRecyclerAdapter != adapter)
                mWrapRecyclerAdapter?.notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            if (adapter == null) return
            if (mWrapRecyclerAdapter != adapter)
                mWrapRecyclerAdapter?.notifyItemRangeChanged(positionStart, itemCount, payload)
        }
    }

}