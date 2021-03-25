package com.example.covenantsermons.extensions

import android.content.Context
import androidx.annotation.MainThread
import androidx.arch.core.util.Function
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.covenantsermons.modelClass.Sermon
import com.example.covenantsermons.modelClass.SermonEntity
import com.example.covenantsermons.modelClass.SermonEntity.Companion.fromSermonEntityToSermon
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


fun Date.formatDate():String {

    //O or greater
//    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//        val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
//        val formattedDate=date.format(formatter)
//        date.
//
//        var formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
//        var formattedDate = date.format(formatter)
//    }
    // <O build calendar instance
    //else {

    val dayMonthYearFormat = SimpleDateFormat("dd-M-yyyy")
    val date: Date = dayMonthYearFormat.parse(this.toString())
    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.toString()
    //}
}

fun String.timeStampToDate():String{
    val firstTimeStampSubstring = this.substringAfter("=", this)
    val lastTimeStampSubstring = firstTimeStampSubstring.substringBefore(",", firstTimeStampSubstring)
    Timber.i("substring= $lastTimeStampSubstring")
    val dayMonthYearFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val calendar=Calendar.getInstance()
    calendar.timeInMillis = lastTimeStampSubstring.toLong()*1000
    return dayMonthYearFormat.format(calendar.time)

}


fun Sermon.serializeToJson(): String {
    val gson = Gson()
    return gson.toJson(this)
}

fun String.deserializeFromJson(): Sermon? {
    val gson = Gson()
    return gson.fromJson(this, Sermon::class.java)
}

fun String.httpsRefToStorageRef():StorageReference {
    val localPath = FirebaseStorage.getInstance().getReferenceFromUrl(this)
    return localPath
}

fun String.pathToName():String {
    val uri=this.toUri()
    val path = uri.path
    val imageSegment: Int = path!!.lastIndexOf('/')
    var imageName: String? =null
    if (imageSegment != -1) {
        imageName = path.substring(imageSegment + 1);
    }
    Timber.i("imageName in pathToImageName ${imageName.toString()}")
    return imageName.toString()
}

fun Sermon.dateToUnderscoredDate():String?=
        this.date?.replace("/","_")


fun Sermon.createImagePath():String{
   val stringBuilder=StringBuilder()
           stringBuilder.apply{
        stringBuilder.append(this@createImagePath.date)
        stringBuilder.append(this@createImagePath.title)
        stringBuilder.append(this@createImagePath.image?.pathToName())
    }
    return stringBuilder.toString()
}

fun ArrayList<SermonEntity>.combineSermonLists(sermonArrayList: ArrayList<Sermon?>):ArrayList<Sermon?> {
    Timber.i("combineSermonLists called")
    Timber.i("sermonArrayList= $sermonArrayList")
    Timber.i("sermonEntityArrayList= $this")
    val sermonArrayListModified = sermonArrayList
    val sermonEntityIterator = this.listIterator()
//    val sermonIterator = sermonArrayList?.listIterator()
    while (sermonEntityIterator.hasNext()) {
        Timber.i("sermonEntityIterator.hasNext() called")
        var sermonEntity = sermonEntityIterator.next()
        Timber.i("sermonEntity= $sermonEntity")
        val sermonIterator = sermonArrayList?.listIterator()
        var sermonEntityDate = sermonEntity.date
        while (sermonIterator!!.hasNext()) {
            Timber.i("sermonIterator.hasNext() called")
            var sermonIndex = sermonIterator.nextIndex()
            var sermon = sermonIterator.next()
            Timber.i("sermon= $sermon")
            var sermonDate = sermon!!.date
            if (sermonEntityDate == sermonDate) {
                Timber.i("sermonEntityDate == sermonDate $sermonEntityDate")
                Timber.i("sermonIndex= $sermonIndex")
                sermonArrayListModified!![sermonIndex] = fromSermonEntityToSermon(sermonEntity)
            }
        }
    }
    return sermonArrayListModified
}

fun ArrayList<Sermon>.sermonInCombinedList(sermon: Sermon?):Boolean{
    this.forEach{
        if(it.date==sermon?.date){
            return true
        }
    }
    return false
}

fun Context.createRootStoragePath()=this.filesDir.toString()+"/"

fun Context.createDir(filePath: String)= File(this.filesDir, filePath)
    //val inputStream= FileInputStream(dir)

fun File.createDirFileName(file: File, fileName: String)=File(file, fileName)






//@MainThread
//@NonNull
//public static <X, Y> LiveData<Y> map(
//@NonNull LiveData<X> source,
//@NonNull final Function<X, Y> mapFunction) {
//    final MediatorLiveData<Y> result = new MediatorLiveData<>();
//    result.addSource(source, new Observer < X >() {
//        @Override
//        public void onChanged(@Nullable X x) {
//            result.setValue(mapFunction.apply(x));
//        }
//    });
//    return result;
//}


@MainThread
fun <X, Y> liveDataToSermonArrayList(
        source: LiveData<X>,
//        mapFunction: Function<X?, Y>): LiveData<Y> {
        mapFunction: Function<X?, Y>): ArrayList<Sermon> {
    Timber.i("liveDataToSermonarrayList called")
    val result = MediatorLiveData<Y>()
    val sermonTransformedList=ArrayList<Sermon>()
    result.addSource(source) { x ->
        Timber.i("in liveDataToSermonArrayList fun x= $x")
        result.value = mapFunction.apply(x)
            sermonTransformedList.add(x as Sermon)}
//    return result
    return sermonTransformedList
}



//@MainThread
//fun <X, Y> liveDataToSermonArrayList(
//        source: LiveData<X>,
//        mapFunction: Function<X?, Y>): LiveData<Y> {
//    val result = MediatorLiveData<Y>()
//    val sermonTransformedList=ArrayList<Sermon>()
//    result.addSource(source, Observer<X>() {
//        override fun onChanged(x: X) {
//            result.value(mapFunction.apply(x))
//        }
//    })
//    }) {x ->
//        override fun onChanged (x: X){
//            result.value(mapFunction.apply(x))
//        }
//    }
////    { x -> result.value = mapFunction.apply(x)
////                                sermonTransformedList.add(x)}
//    return result
//}

//@MainThread
//@NonNull
//fun <T>Transformations.liveDataToList(@NonNull sermonList: LiveData<T>): ArrayList<Sermon>{
//    val transformedSermonList:ArrayList<Sermon> = ArrayList()
//
//    //{ sermonList -> sermonList.forEach(transformedSermonList.add(it)) }.apply(x)
//
//
//    sermonList.forEach(it -> transformedSermonList.add(it))
//
//
//
//    return ArrayList<Sermon>()
//}
//
//
//        @MainThread
//        @NonNull
//        public static <X, Y> LiveData<Y> map(
//@NonNull LiveData<X> source,
//@NonNull final Function<X, Y> mapFunction) {
//    final MediatorLiveData<Y> result = new MediatorLiveData<>();
//    result.addSource(source, new Observer<X>() {
//        @Override
//        public void onChanged(@Nullable X x) {
//            result.setValue(mapFunction.apply(x));
//        }
//    });
//    return result;
//}
