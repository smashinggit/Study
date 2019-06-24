package com.cs.architecture

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cs.architecture.jetpack.databinding.BindingActivity
import com.cs.architecture.jetpack.lifecycle.MyLifeCycleObserver
import com.cs.architecture.mvp.MvpActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnMvp.setOnClickListener { startActivity(Intent(this, MvpActivity::class.java)) }
        btnDatabinding.setOnClickListener { startActivity(Intent(this, BindingActivity::class.java)) }
//        btnMvvm.setOnClickListener { startActivity(Intent(this, MvvmActivity::class.java)) }


        //监听所有Activity的声明周期回调
        application.registerActivityLifecycleCallbacks(ActivityLifeCycleListener())


        //监听将此Activity的声明周期
        val observer = MyLifeCycleObserver(this, lifecycle) { }
        lifecycle.addObserver(observer)
    }


}
