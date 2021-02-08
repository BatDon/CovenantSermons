package com.example.covenantsermons.extensions

import androidx.annotation.MainThread
import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.covenantsermons.modelDatabase.Sermon
import timber.log.Timber
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
