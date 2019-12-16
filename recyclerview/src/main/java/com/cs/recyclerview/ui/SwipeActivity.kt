package com.cs.recyclerview.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs.recyclerview.R
import com.cs.recyclerview.adapter.SwipeAdapter
import com.cs.recyclerview.ui.decoration.DividerDecoration
import com.cs.recyclerview.ui.touch.SwipeCallBack
import kotlinx.android.synthetic.main.activity_loadpic.*

/**
 * @Author : ChenSen
 * @Date : 2019/9/26 9:49
 *
 * @Desc :
 */
class SwipeActivity : AppCompatActivity() {


    private val adapter by lazy {
        SwipeAdapter(this, list)
    }

    private val list = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe)

        init()
    }

    private fun init() {

        for (i in 0 until 100) {
            list.add("item $i")
        }

        rvList.adapter = adapter
        rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvList.addItemDecoration(DividerDecoration(this, LinearLayoutManager.VERTICAL))

        val itemTouchHelper = ItemTouchHelper(SwipeCallBack(adapter))
        itemTouchHelper.attachToRecyclerView(rvList)
    }
}