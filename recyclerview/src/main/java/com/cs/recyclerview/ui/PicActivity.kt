package com.cs.recyclerview.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSONObject
import com.cs.common.utils.toast
import com.cs.recyclerview.R
import com.cs.recyclerview.adapter.MeizhIAdapter
import com.cs.recyclerview.data.Meizhi
import com.cs.recyclerview.data.MeizhiRepository
import com.cs.recyclerview.ui.decoration.MarginDecoration
import kotlinx.android.synthetic.main.activity_loadpic.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @Author : ChenSen
 * @Date : 2019/9/25 16:30
 *
 * @Desc : 加载大量网络图片
 */
class PicActivity : AppCompatActivity() {


    private val adapter by lazy {
        MeizhIAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loadpic)

        init()
        loadData()
    }


    private fun init() {
        rvList.adapter = adapter

        val gridLayoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        rvList.layoutManager = gridLayoutManager
        rvList.addItemDecoration(MarginDecoration(left = 10, top = 10, right = 10, bottom = 10))


        rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            //用来标记是否正在向上滑动
            var isSlidingUp = false

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val lastVisiblePosition = gridLayoutManager.findLastCompletelyVisibleItemPosition()
                    val itemCount = gridLayoutManager.itemCount

                    // 判断是否滑动到了最后一个item，并且是向上滑动
                    if (lastVisiblePosition == (itemCount - 1) && isSlidingUp) {

                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
                isSlidingUp = dy > 0
            }
        })
    }

    private fun loadData() {

        val call = MeizhiRepository.getInstance().getMeizhi(1)
        call.enqueue(object : Callback<JSONObject> {
            override fun onFailure(call: Call<JSONObject>, t: Throwable) {
                toast("获取数据失败！${t.message}")
            }

            override fun onResponse(call: Call<JSONObject>, response: Response<JSONObject>) {

                if (response.isSuccessful) {

                    val jsonObject = response.body()
                    val jsonArray = jsonObject?.getJSONArray("results")

                    if (jsonArray.isNullOrEmpty()) {
                        toast("获取数据为空！}")
                    } else {
                        val list = jsonArray.toJavaList(Meizhi::class.java)
                        adapter.submitList(list)
                    }

                } else {
                    toast("获取数据失败！${response.errorBody()?.string()}")
                }
            }
        })
    }

}