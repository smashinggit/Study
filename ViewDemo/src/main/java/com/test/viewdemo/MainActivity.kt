package com.test.viewdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.test.viewdemo.activity.GestureDetectorActivity
import com.test.viewdemo.activity.ScrollConflictActivity
import com.test.viewdemo.activity.VelocityActivity
import com.test.viewdemo.activity.ViewScrollActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnViewScroll.setOnClickListener { startActivity(Intent(MainActivity@ this, ViewScrollActivity::class.java)) }
        btnDetector.setOnClickListener { startActivity(Intent(MainActivity@ this, GestureDetectorActivity::class.java)) }
        btnVelocity.setOnClickListener { startActivity(Intent(MainActivity@ this, VelocityActivity::class.java)) }
        btnScrollConflict1.setOnClickListener { startActivity(Intent(MainActivity@ this, ScrollConflictActivity::class.java)) }



        mView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->

            log("View的坐标信息" +
                    "\n   left : ${mView.left}  " +
                    "\n   top :  ${mView.top}   " +
                    "\n   right :  ${mView.right}   " +
                    "\n   bottom :  ${mView.bottom}   " +
                    "\n   translationX :  ${mView.translationX}   " +
                    "\n   translationT :  ${mView.translationY}   " +
                    "\n   x :  ${mView.x}   " +
                    "\n   y :  ${mView.y}   ")


            mView.translationX = 100f
            mView.translationY = 100f

            log("移动后View的坐标信息" +
                    "\n   left : ${mView.left}  " +
                    "\n   top :  ${mView.top}   " +
                    "\n   right :  ${mView.right}   " +
                    "\n   bottom :  ${mView.bottom}   " +
                    "\n   translationX :  ${mView.translationX}   " +
                    "\n   translationT :  ${mView.translationY}   " +
                    "\n   x :  ${mView.x}   " +
                    "\n   y :  ${mView.y}   ")


        }


    }

    fun Context.log(msg: String) {
        Log.d("tag", msg)
    }
}