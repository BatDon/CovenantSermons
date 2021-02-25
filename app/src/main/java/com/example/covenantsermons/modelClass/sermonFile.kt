package com.example.covenantsermons.modelClass

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//@Entity(tableName = "sermon_table")
//data class Sermon(@PrimaryKey @ColumnInfo(name = "date") val date: String)

@Entity(tableName = "sermon_table")
data class SermonEntity(
    @PrimaryKey
    @NonNull
    var date: String = "",
    var title: String? = "",
    var pastorName: String? = "",
    var audioFile: String? = "",
    var duration: Int? = 0,
    var image: String? = ""
){
    companion object{
        fun fromSermonEntityToSermon(sermonEntity:SermonEntity)=Sermon(
            date=sermonEntity.date,
            title=sermonEntity.title,
            pastorName = sermonEntity.pastorName,
            audioFile = sermonEntity.audioFile,
            duration = sermonEntity.duration,
            image = sermonEntity.image)

        fun fromSermonEntityToSermon(sermonEntityList :List<SermonEntity>)=sermonEntityList.map{ SermonEntity.fromSermonEntityToSermon(it)}
    }

    fun fromSermonToSermonEntity()=SermonEntity(
        date=this.date,
        title=this.title,
        pastorName=this.pastorName,
        audioFile=this.audioFile,
        duration=this.duration,
        image=this.image
    )
}


@Parcelize
data class Sermon(
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




//@Entity(tableName = "sermon_table")
////@Parcelize
//data class Sermon(
//    @PrimaryKey
//    @NonNull
//    var date: String? = "",
//    var title: String? = "",
//    var pastorName: String? = "",
//    var audioFile: String? = "",
//    var duration: Int? = 0,
//    var image: String? = ""
//)


//): Parcelable {
//    init {
//        title = title ?: ""
//        pastorName = pastorName ?: ""
//        audioFile = audioFile ?: ""
//        duration = duration?:0
//        image = image ?: ""
//    }
//}

