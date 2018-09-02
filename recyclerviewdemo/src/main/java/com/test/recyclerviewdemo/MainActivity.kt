package com.test.recyclerviewdemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.test.recyclerviewdemo.activity.Activity1
import com.test.recyclerviewdemo.activity.XRecyclerViewActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn1.setOnClickListener { startActivity(Intent(this, Activity1::class.java)) }
        btn2.setOnClickListener { startActivity(Intent(this, XRecyclerViewActivity::class.java)) }
    }
}
