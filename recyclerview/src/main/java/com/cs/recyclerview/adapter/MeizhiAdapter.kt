package com.cs.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cs.recyclerview.R
import com.cs.recyclerview.data.Meizhi
import kotlinx.android.synthetic.main.item_meizhi.view.*

/**
 * @Author : ChenSen
 * @Date : 2019/9/23 15:06
 *
 * @Desc :
 */
class MeizhIAdapter(private val context: Context) :
        ListAdapter<Meizhi, MeizhIAdapter.MeizhiHolder>(MeizhiItemCallBack()) {

    private val mList = ArrayList<Meizhi>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeizhiHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_meizhi, parent, false)
        return MeizhiHolder(view)
    }

    override fun onBindViewHolder(holder: MeizhiHolder, position: Int) {
        val meizhi = getItem(position)

        meizhi.url?.let {
            Glide.with(context)
                    .asDrawable()
                    .load(it)
                    .into(holder.itemView.ivPic)
        }
    }


    fun addAll(list: List<Meizhi>) {
        mList.addAll(list)

        val newList = ArrayList<Meizhi>()
        newList.addAll(mList)

        submitList(newList)
    }

    fun clearAddAll(list: List<Meizhi>) {
        mList.clear()
        addAll(list)
    }

    class MeizhiHolder(item: View) : RecyclerView.ViewHolder(item) {}
}

class MeizhiItemCallBack : DiffUtil.ItemCallback<Meizhi>() {
    override fun areItemsTheSame(oldItem: Meizhi, newItem: Meizhi): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: Meizhi, newItem: Meizhi): Boolean {
        return oldItem == newItem
    }

}