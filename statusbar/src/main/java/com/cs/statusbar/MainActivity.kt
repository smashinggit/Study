package com.cs.statusbar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fullScreen.setOnClickListener { startActivity(Intent(this, FullScreenActivity::class.java)) }
        fullScreenWithText.setOnClickListener { startActivity(Intent(this, FullScreenWithTextActivity::class.java)) }
        sameColorWithTitleBar.setOnClickListener { startActivity(Intent(this, SameColorWithTitleBarActivity::class.java)) }

        //思考一、Activity中window是怎么回事？里面有什么View/ViewGroup
        printChildView(window.decorView as ViewGroup)
    }

    private fun printChildView(viewGroup: ViewGroup) {
        Log.d("tag", "ViewGroup  ${viewGroup.javaClass.simpleName} 的子view数量为 ${viewGroup.childCount}")
        for (i in 0 until viewGroup.childCount)
            Log.d("tag", "ChildView  ${viewGroup.getChildAt(i).javaClass.simpleName} ")

        (0 until viewGroup.childCount)
                .filter { viewGroup.getChildAt(it) is ViewGroup }
                .forEach { printChildView(viewGroup.getChildAt(it) as ViewGroup) }
    }

}