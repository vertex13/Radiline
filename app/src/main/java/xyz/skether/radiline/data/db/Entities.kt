package xyz.skether.radiline.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "station")
data class DBStation(
    @PrimaryKey var id: Long,
    var genreId: Long,
    var name: String,
    var mediaType: String,
    var bitRate: Int,
    var genreName: String?,
    var currentTrack: String?,
    var numberListeners: Int,
    var logo: String?,
    var updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "genre")
data class DBGenre(
    @PrimaryKey var id: Long,
    var parentId: Long?,
    var name: String,
    var hasChildren: Boolean,
    var updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "tune_in")
data class DBTuneIn(
    @PrimaryKey var base: String,
    var resource: String,
    var updatedAt: Long = System.currentTimeMillis()
)
