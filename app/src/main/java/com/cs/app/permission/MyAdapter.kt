package com.cs.app.permission

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cs.app.R

/**
 *
 * author : ChenSen
 * data : 2019/6/6
 * desc:
 */
class MyAdapter(private val context: Context, private val contacts: List<Contact>) : RecyclerView.Adapter<MyHolder>() {


    override fun getItemCount() = contacts.size

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_contact, p0, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, p1: Int) {
        holder.tvName.text = contacts[p1].name
        holder.tvNum.text = contacts[p1].phone
    }

}

class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvName = view.findViewById<TextView>(R.id.tvName)
    val tvNum = view.findViewById<TextView>(R.id.tvNum)
}