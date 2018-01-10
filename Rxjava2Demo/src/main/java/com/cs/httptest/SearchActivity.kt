package com.cs.httptest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity() {

    lateinit var mPublishSubject: PublishSubject<String>
    var mCompositeDisposable = CompositeDisposable()
    lateinit var mObserver: DisposableObserver<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mPublishSubject.onNext(s.toString()!!)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        mObserver = object : DisposableObserver<String>() {
            override fun onComplete() {
            }

            override fun onError(e: Throwable) {
            }

            override fun onNext(t: String) {
                Log.d("tag", "onNext " + t)
                tvRestlt.text = t
            }
        }

        mPublishSubject = PublishSubject.create()
        mPublishSubject.debounce(300, TimeUnit.MILLISECONDS)
                .filter {
                    it.isNotEmpty()
                }
                .switchMap {
                    var serachStr = it
                    Observable.create<String> {
                        Log.d("tag", "开始请求，关键词为：" + serachStr)
                        try {
                            Thread.sleep((100 + (Math.random() * 500)).toLong())
                        } catch (e: Exception) {

                        }
                        it.onNext("搜索完毕,关键词为: " + serachStr)
                        it.onComplete()
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)



        mCompositeDisposable.add(mObserver)


    }


    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.clear()
    }

}