package com.cs.httptest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_retrywhen.*
import java.util.concurrent.TimeUnit

/**
 * Created by Lenovo on 2018/1/11.
 */
class RetryWhenActivity : AppCompatActivity() {

    lateinit var mCompositeDisposable: CompositeDisposable
    var faileCount = 0
    var result = StringBuffer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrywhen)

        btnStart.setOnClickListener {
            faileCount = 0
            startRetry()
        }

        mCompositeDisposable = CompositeDisposable()
    }
    //retryWhen是收到onError后触发是否要重订阅的询问，而repeatWhen是通过onComplete触发。
    private fun startRetry() {
        val observable = Observable.create<String> {
            doWork()

            //模拟请求的结果，前四次都返回失败，并将失败信息递交给retryWhen。
            if (faileCount < 5) {
                Log.d("tag", "模拟错误")
                result.append("模拟出现错误 \n")
                it.onError(Throwable("请求出现错误"))
            } else {
                //模拟请求成功的情况
                it.onNext("请求成功")
                it.onComplete()
            }
        }.retryWhen {

            //Function的输入是一个Observable<Throwable>，输出是一个泛型ObservableSource<?>。
            // 如果我们接收Observable<Throwable>发送的消息，
            // 那么就可以得到上游发送的错误类型，并根据该类型进行响应的处理。

            return@retryWhen it.flatMap {
                Log.d("tag", "发生错误 重新尝试")
                result.append("发生错误 第${faileCount}次尝试 \n")

                faileCount++
                //如果该ObservableSource返回onComplete/onError，那么不会触发重订阅；如果发送onNext，那么会触发重订阅。
                return@flatMap Observable.timer(1, TimeUnit.SECONDS)
            }
        }

        var disposableObserver = object : DisposableObserver<String>() {
            override fun onError(e: Throwable) {
                Log.d("tag", "onError")
            }

            override fun onComplete() {
                Log.d("tag", "onComplete")
                result.append("收到正确结果")
                tvResult.text = result
            }

            override fun onNext(t: String) {
                Log.d("tag", "onNext")
            }
        }

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)
        mCompositeDisposable.add(disposableObserver)

    }

    private fun doWork() {
        var time = Math.random() * 500 + 500
        Thread.sleep(time.toLong())
    }

    override fun onDestroy() {
        mCompositeDisposable.clear()
        super.onDestroy()
    }
}