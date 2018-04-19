package com.test.viewdemo.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.test.viewdemo.R
import kotlinx.android.synthetic.main.activity_scrollconflit.*

/**
 *
 * author : ChenSen
 * data : 2018/4/16
 * desc:
 */
class ScrollConflictActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrollconflit)


        recyclerView1.adapter = MyAdapter(this)
        recyclerView2.adapter = MyAdapter(this)
        recyclerView3.adapter = MyAdapter(this)

        recyclerView1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView3.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }


    class MyAdapter(var context: Context) : RecyclerView.Adapter<MyAdapter.MyHolder>() {
        override fun onBindViewHolder(holder: MyHolder?, position: Int) {

            holder?.textView?.text = "this is item $position"
        }

        override fun getItemCount(): Int {
            return 20
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false)
            return MyHolder(view)
        }


        class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var textView = itemView.findViewById<TextView>(R.id.textViewItem)
        }
    }


}