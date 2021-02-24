package com.example.covenantsermons.modelClass

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//@Entity(tableName = "sermon_table")
//data class Sermon(@PrimaryKey @ColumnInfo(name = "date") val date: String)

@Entity(tableName = "sermon_table")
@Parcelize
data class Sermon(
    @PrimaryKey
    var date: String? = "",
    var title: String? = "",
    var pastorName: String? = "",
    var audioFile: String? = "",
    var duration: Int? = 0,
    var image: String? = ""

): Parcelable {
    init {
        date = date ?: ""
        title = title ?: ""
        pastorName = pastorName ?: ""
        audioFile = audioFile ?: ""
        duration = duration?:0
        image = image ?: ""
    }
}