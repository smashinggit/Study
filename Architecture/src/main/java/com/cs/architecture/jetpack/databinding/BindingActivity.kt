package com.cs.architecture.jetpack.databinding

import androidx.databinding.*
import android.os.Bundle
import android.view.View
import android.widget.ImageView
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
 *
 * 注：Android Studio 3.1 and higher allow you to replace observable fields with LiveData objects,
 * which provide additional benefits to your app
 *
 * 方法3，Observable collections
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


    //Specify a custom method name
//    @BindingMethods(value =
//    [BindingMethod(type = ImageView::class, attribute = "android:tint", method = "setImageTintList")])
//

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