package com.cs.httptest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.cs.httptest.http.Api
import com.cs.httptest.http.HttpHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_zip.*

/**
 * Created by Lenovo on 2018/1/10.
 */
class ZipActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zip)

        btnGet.setOnClickListener { getData() }

    }

    private fun getData() {
        val api = HttpHelper.create(Api::class.java)
        var androidObserver = api.getGankData("Android", 5, 1)
        var iosObserver = api.getGankData("iOS", 5, 1)

        androidObserver
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d("tag", "it -" + it)
                }, { Log.d("tag", "cuowu -" + it) })

//        Observable.zip(androidObserver, iosObserver, BiFunction<JSONObject, JSONObject, ArrayList<GankData>> { t1, t2 ->
//            Log.d("tag", "t1- " + t1)
//            Log.d("tag", "t2- " + t2)
////            var gson = Gson()
////            var androidData = ArrayList<GankData>()
////            var iosData = ArrayList<GankData>()
////            t1.getJSONArray("results").let {
////                (0 until it.length()).mapTo(androidData) { i -> gson.fromJson(it.getJSONObject(i).toString(), GankData::class.java) }
////            }
////            t2.getJSONArray("results").let {
////                (0 until it.length()).mapTo(iosData) { i -> gson.fromJson(it.getJSONObject(i).toString(), GankData::class.java) }
////            }
////
//            var datas = ArrayList<GankData>()
////            datas.addAll(androidData)
////            datas.addAll(iosData)
//
//            datas
//        }
//        ).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    Log.d("Tag", "结果- " + it)
//                }
//

    }


}