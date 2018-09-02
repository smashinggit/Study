package com.cmos.agerademo

import android.view.View
import com.google.android.agera.BaseObservable

/**
 *
 * author : ChenSen
 * data : 2018/6/25
 * desc:
 */
class ViewClickObservable : BaseObservable(), View.OnClickListener {


    override fun onClick(v: View?) {
        dispatchUpdate()
    }
}