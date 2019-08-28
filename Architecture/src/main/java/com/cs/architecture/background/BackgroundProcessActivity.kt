package com.cs.architecture.background

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.work.*
import com.cs.architecture.background.workmanager.SaveImageToFileWorker
import com.cs.architecture.background.workmanager.UploadWorker
import com.cs.common.base.BaseActivity
import java.util.concurrent.TimeUnit

/**
 *
 * author : ChenSen
 * data : 2019/8/1
 * desc:
 */
class BackgroundProcessActivity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.M) //最小版本要求23
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        workerManagerStart()
        chainingWork()


    }


    /**
     * WorkManager
     */
    @RequiresApi(Build.VERSION_CODES.M) //最小版本要求23
    fun workerManagerStart() {


        //场景一、约束条件
        val constraints = Constraints.Builder()
                .setRequiresDeviceIdle(true)  //设备空闲
                .setRequiresCharging(true)    //充电状态
                .build()

        //场景三、传递输入数据 注意，此数据最大为10KB
        val data = workDataOf(Pair("IMAGE_URL", "https://clubimg.club.vmall.com/data/attachment/forum/201905/09/173444wicar4nkr98js2gd.jpg"))


        val workRequest = OneTimeWorkRequestBuilder<UploadWorker>()
                .setConstraints(constraints)
                .setInputData(data) //输入数据
                .setInitialDelay(10, TimeUnit.MICROSECONDS) //场景二，当所有条件都满足时，延迟一段时间执行
                .build()

        //场景五、循环任务  注：最小的时间间隔是15分钟
        val periodicWorkRequest = PeriodicWorkRequestBuilder<SaveImageToFileWorker>(1, TimeUnit.HOURS)
                .build()



        WorkManager.getInstance(this).enqueue(workRequest)

        //场景四、监听状态
        WorkManager.getInstance(this)
                .getWorkInfoByIdLiveData(workRequest.id)
                .observe(this, Observer {
                    Toast.makeText(this, "获取数据成功", Toast.LENGTH_SHORT).show()
                })
    }

    /**
     * 顺序执行任务
     */
    private fun chainingWork() {

        val request1 = OneTimeWorkRequestBuilder<UploadWorker>()
                .build()

        val request2 = OneTimeWorkRequestBuilder<UploadWorker>()
                .build()

        val request3 = OneTimeWorkRequestBuilder<UploadWorker>()
                .setInputMerger(ArrayCreatingInputMerger::class.java)  //请求1,2的结果会被当做入参传入请求3中
                .build()                                               //可以用 OverwritingInputMerger 或ArrayCreatingInputMerger
        //合并两个入参


        //同时执行任务1,2 然后在执行任务3
        WorkManager.getInstance(this)
                .beginWith(listOf(request1, request2))
                .then(request3)
                .enqueue()
    }

}