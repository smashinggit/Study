package com.cs.recyclerview.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.cs.recyclerview.R
import com.cs.recyclerview.adapter.DragAdapter
import com.cs.recyclerview.ui.decoration.GridDividerDecoration
import com.cs.recyclerview.ui.touch.DragCallBack
import kotlinx.android.synthetic.main.activity_loadpic.*

/**
 * @Author : ChenSen
 * @Date : 2019/9/26 9:50
 *
 * @Desc :
 */
class DragActivity : AppCompatActivity() {


    private val adapter by lazy {
        DragAdapter(this, list)
    }

    private val list = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag)

        init()
    }

    private fun init() {

        for (i in 0 until 100) {
            list.add("item $i")
        }

        rvList.adapter = adapter
        rvList.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
//        rvList.addItemDecoration(GridDividerDecoration(this))

        val itemTouchHelper = ItemTouchHelper(DragCallBack(adapter))
        itemTouchHelper.attachToRecyclerView(rvList)
    }
}