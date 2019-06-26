package com.cs.architecture.jetpack.livedata

import androidx.arch.core.util.Function
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

/**
 *
 * author : ChenSen
 * data : 2019/6/26
 * desc:
 *
 * Lifecycle 提供了工具类 Transformations 来对 LiveData 的数据类型进行转换，
 * 可以在 LiveData 在数据返回给观察者之前修改 LiveData 中数据的具体类型，
 * 比如 int 型数字 1、2 等转化为中文大写壹、贰等
 *
 * map 和 switchMap 方法唯一的区别是 map 将 LiveData 数据转换为具体的类型，
 * 如上述代码中的 String，而 switchMap 则是 LiveData
 */
class MapViewModel : ViewModel() {

    var mPrice = MutableLiveData<Int>()


    //map 注意，function中的转换将会在主线程中执行
    //将 mPrice 中的int转换为String                                  //返回String
    var mMapPrice = Transformations.map(mPrice) { input -> "$input" }


    //SwitchMap
    val mSwitchMapPrice = Transformations.switchMap(mPrice) {
        //返回LiveData
        val data = MutableLiveData<Int>()
        data.postValue(it)
        return@switchMap data
    }

}