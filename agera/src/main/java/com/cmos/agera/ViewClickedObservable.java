package com.cmos.agera;

import android.view.View;

import com.google.android.agera.BaseObservable;

/**
 * author : ChenSen
 * data : 2018/9/7
 * desc:
 */
public class ViewClickedObservable extends BaseObservable implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        dispatchUpdate();
    }
}
