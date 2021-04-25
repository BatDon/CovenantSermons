package com.batdon.covenantsermons.modelClass

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.batdon.covenantsermons.modelClass.SermonEntity.Companion.DOWNLOADING_STATE_IMAGES_DOWNLOAD
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
        var image: String? = "",
        var downloadingButtonImage: String? = DOWNLOADING_STATE_IMAGES_PLAY
) {
    companion object {
        fun fromSermonEntityToSermon(sermonEntity: SermonEntity) = Sermon(
                date = sermonEntity.date,
                title = sermonEntity.title,
                pastorName = sermonEntity.pastorName,
                audioFile = sermonEntity.audioFile,
                duration = sermonEntity.duration,
                image = sermonEntity.image,
                downloadingButtonImage = sermonEntity.downloadingButtonImage)

        fun fromSermonEntityToSermon(sermonEntityList: List<SermonEntity>) = sermonEntityList.map { SermonEntity.fromSermonEntityToSermon(it) }

        fun fromSermonToSermonEntity(sermon:Sermon) = SermonEntity(
                date = sermon.date!!,
                title = sermon.title,
                pastorName = sermon.pastorName,
                audioFile = sermon.audioFile,
                duration = sermon.duration,
                image = sermon.image,
                downloadingButtonImage = sermon.downloadingButtonImage
        )
        fun fromSermonToSermonEntity(sermonList: List<Sermon>) = sermonList.map { SermonEntity.fromSermonToSermonEntity(it) }

        const val DOWNLOADING_STATE_IMAGES_DOWNLOAD: String="DOWNLOADING_STATE_IMAGES_DOWNLOAD"
        const val DOWNLOADING_STATE_IMAGES_CANCEL: String="DOWNLOADING_STATE_IMAGES_CANCEL"
        const val DOWNLOADING_STATE_IMAGES_PLAY: String="DOWNLOADING_STATE_IMAGES_PLAY"
    }

//    fun fromSermonToSermonEntity(sermon:Sermon) = SermonEntity(
//            date = this.date,
//            title = this.title,
//            pastorName = this.pastorName,
//            audioFile = this.audioFile,
//            duration = this.duration,
//            image = this.image,
//            downloadingButtonImage = this.downloadingButtonImage
//    )
}


@Parcelize
data class Sermon(
        var date: String? = "",
        var title: String? = "",
        var pastorName: String? = "",
        var audioFile: String? = "",
        var duration: Int? = 0,
        var image: String? = "",
        var downloadingButtonImage: String? = DOWNLOADING_STATE_IMAGES_DOWNLOAD

) : Parcelable {
    init {
        date = date ?: ""
        title = title ?: ""
        pastorName = pastorName ?: ""
        audioFile = audioFile ?: ""
        duration = duration ?: 0
        image = image ?: ""
    }

}


//enum class DownloadingStateImages {
//    val DOWNLOAD:Int=1,
//    val CANCEL=2,
//    val PLAY=3;
//}


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

