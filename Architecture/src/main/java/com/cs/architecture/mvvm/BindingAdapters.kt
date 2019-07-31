package com.cs.architecture.mvvm

import android.widget.RadioButton
import androidx.databinding.BindingAdapter
import com.cs.architecture.R

/**
 *
 * author : ChenSen
 * data : 2019/7/15
 * desc:
 */
object BindingAdapters {

    @BindingAdapter("app:checkedBySex")
    @JvmStatic
    fun checkedBySex(radioButton: RadioButton, sex: Int) {
        when (radioButton.id) {
            R.id.rbMan -> radioButton.isChecked = sex == 1
            R.id.rbWoman -> radioButton.isChecked = sex == 2
        }
    }

}