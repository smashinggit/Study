package com.cs.httptest

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        btnZip.setOnClickListener {
            startActivity(Intent(this, ZipActivity::class.java))
        }



        test()


    }

    private fun test() {
        //        Observable.just(1, 2, 3, 4)
//                .switchMap {
//                    Observable.just(it).subscribeOn(Schedulers.newThread())
//                }
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                   // Log.d("tag", "结果 " + it)
//                }


//        var observable = ReplaySubject.create<Int>()
//        observable.onNext(1)
//        observable.onNext(2)
//        observable.onNext(3)
//        observable.onComplete()
//        observable.subscribe {
//            Log.d("tag", "结果 " + it)
//        }
//        observable.subscribe {
//            Log.d("tag", "结果2 " + it)
//        }
    }

}

