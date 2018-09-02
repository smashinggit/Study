package com.cs.study.view

import android.content.Context
import android.graphics.Color
import android.support.annotation.NonNull
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.ViewConfiguration

/**
 *
 * author : ChenSen
 * data : 2018/4/20
 * desc:
 */
class SuperRefreshLayout : SwipeRefreshLayout {

    var isRefresh = false
    var mTouchSlop = 0


    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor (context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

//    constructor (context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
//        init(context)
//    }

    private fun init(context: Context) {
        setColorSchemeColors(Color.BLUE)
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    fun setAdapter(@NonNull recyclerView: RecyclerView) {


    }

}