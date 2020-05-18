package com.cs.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.cs.kotlin.api.Api
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.fastjson.FastJsonConverterFactory
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(FastJsonConverterFactory.create())
                .build()

        GlobalScope.launch(Dispatchers.Main) {
            val repos = retrofit.create(Api::class.java).getRepo("smashinggit")

            textView.text = repos[0].name
            println("第一个仓库名：${repos[0].name}")
        }

        GlobalScope.launch {

        }

        runBlocking {

        }
    }

    private suspend fun work1() {
        withContext(Dispatchers.IO) {
            Thread.sleep(1000) //模拟耗时操作
            println("do work1 in ${Thread.currentThread().name}")
        }
    }

    private suspend fun work2() {
        withContext(Dispatchers.IO) {
            Thread.sleep(1000) //模拟耗时操作
            println("do work2 in ${Thread.currentThread().name}")
        }
    }

    private fun ui1() {
        println("update ui1 in ${Thread.currentThread().name}")
    }

    private fun ui2() {
        println("update ui2 in ${Thread.currentThread().name}")
    }
}
