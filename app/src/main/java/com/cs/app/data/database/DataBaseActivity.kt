package com.cs.app.data.database

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs.app.R
import com.cs.library_architecture.base.BaseActivity
import kotlinx.android.synthetic.main.activity_database.*
import kotlinx.android.synthetic.main.item_user.view.*

/**
 *
 * author : ChenSen
 * data : 2019/7/16
 * desc:
 */
class DataBaseActivity : BaseActivity() {

    private val userDao = UserDao(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

        btnInsert.setOnClickListener {
            val user = User("陈森~${System.currentTimeMillis()}", 1, 18)
            val result = userDao.insert(user)
            if (result) {
                queryUsers()
            }
        }

        btnDelete.setOnClickListener {
            userDao.deleteAll()
            queryUsers()
        }

        btnQuery.setOnClickListener {
            queryUsers()
        }

        queryUsers()
    }

    private fun queryUsers() {
        val result = userDao.queryAll()

        if (result.isNotEmpty()) {
            rvUsers.visibility = View.VISIBLE

            val adapter = UserAdapter(this, result)
            rvUsers.adapter = adapter
            rvUsers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        } else {
            rvUsers.visibility = View.GONE
            Toast.makeText(this, "查询结果为空", Toast.LENGTH_SHORT).show()
        }
    }


    class UserAdapter(val context: Context, val data: List<User>) : RecyclerView.Adapter<UserAdapter.UserHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
            return UserHolder(view)
        }

        override fun getItemCount() = data.size

        override fun onBindViewHolder(holder: UserHolder, position: Int) {

            val user = data[position]
            holder.tvName.text = user.name
            holder.tvAge.text = user.age.toString()
            holder.tvSex.text = if (1 == user.sex) "男" else "女"

        }


        class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvName: TextView = itemView.tvName
            val tvAge: TextView = itemView.tvAge
            val tvSex: TextView = itemView.tvSex
        }
    }

}