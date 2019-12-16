package com.cs.customize.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cs.customize.R
import com.cs.customize.view.stepview.StepViewItemDecoration
import kotlinx.android.synthetic.main.fragment_stepview.*
import java.util.*

class StepViewFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stepview, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val dataList = ArrayList<String>()
        val timeList = ArrayList<String>()

        dataList.add("卖家发货")
        dataList.add("到达【义乌中心】")
        dataList.add("离开【义乌中心】,下一站【郑州汽转】")
        dataList.add("您的快件已被HN丰巢【自取柜】代收,请及时取件。如有问题请联系派件员13888888888")
        dataList.add("已签收，签收人凭取货码签收。感谢使用丰巢 【自取柜】,期待再次为您服务。")

        timeList.add("2018-03-02 20:45:13")
        timeList.add("2018-03-02 07:15:13")
        timeList.add("2018-03-03 20:45:13")
        timeList.add("2018-03-04 08:55:23")
        timeList.add("2018-03-04 12:28:15")

        Collections.reverse(dataList)
        Collections.reverse(timeList)

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = StepViewAdapter(context!!, dataList, timeList)
        recyclerView.addItemDecoration(StepViewItemDecoration(context!!))
    }


    class StepViewAdapter(private var mContext: Context, var data: ArrayList<String>, var time: ArrayList<String>) : RecyclerView.Adapter<StepViewAdapter.StepViewHolder>() {

        override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
            if (position == 0) {
                holder.textView.setTextColor(Color.parseColor("#4BAB6E"))
                holder.tvTime.setTextColor(Color.parseColor("#4BAB6E"))
            } else {
                holder.textView.setTextColor(Color.parseColor("#9B9B9B"))
                holder.tvTime.setTextColor(Color.parseColor("#9B9B9B"))
            }

            holder.tvTime.setTextColor(Color.parseColor("#9B9B9B"))

            holder.textView.text = data[position]
            holder.tvTime.text = time[position]
        }

        override fun getItemCount(): Int = data.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
            val itemView = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview, parent, false)
            return StepViewHolder(itemView)
        }

        class StepViewHolder(item: View) : RecyclerView.ViewHolder(item) {
            var textView: TextView = item.findViewById(R.id.textView_item)
            var tvTime: TextView = item.findViewById(R.id.tvTime_item)

        }

    }
}