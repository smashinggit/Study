package com.cs.architecture

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cs.architecture.jetpack.databinding.BindingActivity
import com.cs.architecture.jetpack.lifecycle.MyLifeCycleObserver
import com.cs.architecture.jetpack.lifecycle.MyLifeCycleObserver8
import com.cs.architecture.jetpack.livedata.LiveDataActivity
import com.cs.architecture.mvp.MvpActivity
import com.cs.architecture.mvp.MvpActivity2
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnMvp.setOnClickListener { startActivity(Intent(this, MvpActivity2::class.java)) }
        btnDatabinding.setOnClickListener { startActivity(Intent(this, BindingActivity::class.java)) }
        btnLiveData.setOnClickListener { startActivity(Intent(this, LiveDataActivity::class.java)) }


        //监听所有Activity的声明周期回调
        application.registerActivityLifecycleCallbacks(ActivityLifeCycleListener())


        //监听将此Activity的声明周期
        val observer = MyLifeCycleObserver(this, lifecycle) { }
        lifecycle.addObserver(observer)

        lifecycle.addObserver(MyLifeCycleObserver8())
    }


}
