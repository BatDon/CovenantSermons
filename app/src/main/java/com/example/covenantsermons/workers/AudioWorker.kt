package com.example.covenantsermons.workers

import android.content.Context
import android.text.TextUtils
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber

//TODO Create interface for both workers to extend from
class AudioWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        val appContext = applicationContext

        val imageUri = inputData.getString(AUDIO_URI)

        return try {
            if (TextUtils.isEmpty(resourceUri)) {
                Timber.e("Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            Result.success(outputData)
        } catch (throwable: Throwable) {
            Timber.e(throwable, "Error downloading image")
            Result.failure()
        }
    }

}