package com.cs.httptest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_polling.*
import java.util.*
import java.util.concurrent.TimeUnit

class PollingActivity : AppCompatActivity() {
    var result = StringBuffer()
    lateinit var mCompositeDisposable: CompositeDisposable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_polling)

        btnSimple.setOnClickListener { doSimplePolling() }
        btnAdvance.setOnClickListener { doAnvancePolling() }

        mCompositeDisposable = CompositeDisposable()
    }


    /**
     * 每2秒执行一次
     */
    private fun doSimplePolling() {

        val observable = Observable.intervalRange(0, 5, 0, 2, TimeUnit.SECONDS)
                .doOnNext {
                    Log.d("tag", "doSimplePolling   doOnNext")
                    doWork()//这里使用了doOnNext，因此DisposableObserver的onNext要等到该方法执行完才会回调。
                }

        var disposableObserver = getDisposiableObserver()

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)

        mCompositeDisposable.add(disposableObserver)


    }


    //retryWhen是收到onError后触发是否要重订阅的询问，而repeatWhen是通过onComplete触发。
    private fun doAnvancePolling() {
        val observable = Observable.just(0L).doOnComplete {
            doWork()
        }.repeatWhen {
            var count = 0
            return@repeatWhen it.flatMap(Function<Any, ObservableSource<Long>> {
                if (++count >= 5)
                    return@Function Observable.error(Throwable("Polling work finished"))////发送onError消息，可以触发下游的onError回调。

                // return@Function Observable.create<Long> { it.onComplete() }
                return@Function Observable.just(0)
                //repeatWhen  如果该Observable发送了onComplete或者onError则表示不需要重订阅，结束整个流程；否则触发重订阅的操作。
            })
        }

        var disposableObserver = getDisposiableObserver()

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)

        mCompositeDisposable.add(disposableObserver)


    }


    private fun getDisposiableObserver(): DisposableObserver<Long> {

        return object : DisposableObserver<Long>() {
            override fun onError(e: Throwable) {
                Log.d("tag", "DisposableObserver  onError ")
                result.append("onError \n")
                tvResult.text = result
            }

            override fun onNext(t: Long) {
                Log.d("tag", "DisposableObserver  onNext ")
                result.append("onNext \n")
                tvResult.text = result
            }

            override fun onComplete() {
                Log.d("tag", "DisposableObserver  onComplete ")
                result.append("onComplete \n")
                tvResult.text = result
                result.delete(0, result.length - 1)
            }

        }

    }


    /**
     * 模拟耗时操作
     */
    private fun doWork() {

        Log.d("tag", "doWork")
        var time = Math.random() * 500 + 500
        Thread.sleep(time.toLong())

    }


    override fun onDestroy() {
        mCompositeDisposable.clear()
        super.onDestroy()
    }


}