package com.test.viewdemo.activity

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import com.test.viewdemo.R
import kotlinx.android.synthetic.main.activity_viewscroll.*

/**
 *
 * author : ChenSen
 * data : 2018/4/12
 * desc:
 */
class ViewScrollActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewscroll)

        btnViewScroll.setOnClickListener { startScrollTo() }
        btnAnim.setOnClickListener { startAnim() }
        btnLayoutParam.setOnClickListener { startLayoutParam() }


        mView.setOnClickListener { Toast.makeText(this, "this is View", Toast.LENGTH_SHORT).show() }
        mViewGroup.setOnClickListener { Toast.makeText(this, "this is ViewGroup", Toast.LENGTH_SHORT).show() }
    }


    private fun startScrollTo() {
        mViewGroup.scrollTo(-100, -50)  //只移动内容
    }


    private fun startAnim() {
        ObjectAnimator.ofFloat(mViewGroup, "translationX", 0f, 500f)
                .setDuration(3000)
                .start()
    }

    private fun startLayoutParam() {
        val layoutParams = mViewGroup.layoutParams as LinearLayout.LayoutParams
        layoutParams.leftMargin += 200
        mViewGroup.layoutParams = layoutParams
    }

    fun Context.log(msg: String) {
        Log.d("tag", msg)
    }
}