package com.cs.architecture.jetpack.databinding

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.cs.architecture.R
import com.cs.architecture.databinding.ActivityBindBinding
import com.cs.common.base.BaseActivity

/**
 *
 * author : ChenSen
 * data : 2019/6/24
 * desc:
 *  双向绑定
 *
 * 数据刷新视图
 * 方法1. 继承BaseObservable
 * 方法2. 使用ObservableField
 * 方法3，使用LiveData
 *
 *
 * Binding adapters
 * 1. Automatic method selection
 * 2. Specify a custom method name
 * 3. Provide custom logic
 *
 */
class BindingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bindBinding = DataBindingUtil.setContentView<ActivityBindBinding>(this, R.layout.activity_bind)

        val user = User(ObservableField("陈森"), ObservableField("18"), ObservableField("china"))
        bindBinding.user = user

        val presenter = EventPresenter(this)
        bindBinding.presenter = presenter
    }

    //Provide custom logic
    @BindingAdapter("android:paddingLeft")
    fun setPaddingLeft(view: View, padding: Int) {
        view.setPadding(padding, view.paddingTop, view.paddingRight, view.paddingBottom)
    }

    @BindingAdapter("imageUrl")
    fun setImageUrl(view: ImageView, url: String) {
        //加载图片
    }
}