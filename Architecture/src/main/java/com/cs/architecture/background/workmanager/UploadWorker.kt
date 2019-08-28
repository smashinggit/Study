package com.cs.architecture.background.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

/**
 *
 * author : ChenSen
 * data : 2019/8/1
 * desc:
 */
class UploadWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    override fun doWork(): Result {

        //场景三、获取数据
        val data = inputData

        // Do the work here--in this case, upload the images.
        uploadImages(data.getString("IMAGE_URL") ?: "")


        //场景四、传递输出数据
        val outData = workDataOf(Pair("result", "success"))
        // Indicate whether the task finished successfully with the Result
        return Result.success(outData)
    }


    private fun uploadImages(url: String) {
        Thread.sleep(3000)
    }
}