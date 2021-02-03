package com.example.covenantsermons.extensions

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
    val dayMonthYearFormat = SimpleDateFormat("MM/dd/yyyy",Locale.getDefault())
    val calendar=Calendar.getInstance()
    calendar.timeInMillis = lastTimeStampSubstring.toLong()*1000
    return dayMonthYearFormat.format(calendar.time)


    //val date = Date(lastTimeStampSubstring.toLong() * 1000)
    //return "something"
}