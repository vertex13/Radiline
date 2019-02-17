package xyz.skether.radiline.domain

class Station(
    val id: Int,
    val name: String,
    val listeners: Int = 1234,
    val bitrate: Int = 128,
    val genre: Genre? = null
)

class Genre(
    val id: Int,
    val name: String,
    val hasSubGenres: Boolean,
    val parent: Genre? = null
) {
    var subGenres: MutableList<Genre>? = null
    var stations: MutableList<Station>? = null
    var areAllStationsLoaded: Boolean = false
}
