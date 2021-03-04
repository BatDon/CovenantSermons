package com.example.covenantsermons.workers

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.covenantsermons.ImageRepository
import com.example.covenantsermons.extensions.deserializeFromJson
import com.example.covenantsermons.modelClass.Sermon
import com.example.covenantsermons.viewmodel.DownloadViewModel.Companion.KEY_SERMON_SERIALIZED
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber
import java.io.FileOutputStream
import java.io.IOException

//class ImageWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params), KoinComponent {
class ImageWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams), KoinComponent {

    init{
       Timber.i("ImageWorker created")
    }

    private val imageRepository: ImageRepository by inject()

    override fun doWork(): Result {
        Timber.i("doWork called")
        val appContext = applicationContext

        val serializedSermon=inputData.getString(KEY_SERMON_SERIALIZED)
        val sermon=serializedSermon?.deserializeFromJson()

//        val imageUri = inputData.getString(KEY_IMAGE_URI)

        return try {
            if (sermon==null) {
                Timber.e("Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }
            else{
                val bitmap=sermon.image?.let { imageRepository.getSermonImage(it) }
                bitmap?.let {
                    saveBitmapToFile(sermon, it)
                }
                val outputBitmap: Data = workDataOf(KEY_IMAGE_BITMAP_FILE_PATH to bitmap)
                Result.success(outputBitmap)
            }


        } catch (throwable: Throwable) {
            Timber.e(throwable, "Error downloading image")
            Result.failure()
        }

    }

    private fun saveBitmapToFile(sermon: Sermon, bitmap: Bitmap):String?{
        return try {
            Timber.i("saveBitmapToFile bitmap= $bitmap")
            val filePath=Uri.Builder().appendPath(sermon.date)
                    .appendPath(sermon.title)
                    .appendPath(sermon.image)
            FileOutputStream(filePath.toString()).use { fileOutputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            }
            filePath.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

//    fun saveBitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
//        //create a file to write bitmap data
//        var file: File? = null
//        return try {
//            file = File(Environment.getExternalStorageDirectory().toString() + File.separator + fileNameToSave)
//            file.createNewFile()
//
//            //Convert bitmap to byte array
//            val bos = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
//            val bitmapdata = bos.toByteArray()
//
//            //write the bytes in file
//            val fos = FileOutputStream(file)
//            fos.write(bitmapdata)
//            fos.flush()
//            fos.close()
//            file
//        } catch (e: Exception) {
//            e.printStackTrace()
//            file // it will return null
//        }
//    }

    companion object{
        const val KEY_IMAGE_BITMAP_FILE_PATH="KEY_IMAGE_BITMAP_FILE_PATH"
    }

}


