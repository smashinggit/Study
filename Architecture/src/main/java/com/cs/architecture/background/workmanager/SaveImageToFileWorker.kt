package com.cs.architecture.background.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 *
 * author : ChenSen
 * data : 2019/8/1
 * desc:
 */
class SaveImageToFileWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        return Result.success()
    }
}