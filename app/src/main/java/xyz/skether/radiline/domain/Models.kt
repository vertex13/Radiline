package xyz.skether.radiline.domain

data class Station(
    val id: Long,
    val name: String,
    val listeners: Int = 1234,
    val bitrate: Int = 128
) {
    var genre: Genre? = null
}

data class Genre(
    val id: Long,
    val name: String,
    val hasSubGenres: Boolean
) {
    var parent: Genre? = null
    var subGenres: MutableList<Genre>? = null
    var stations: MutableList<Station>? = null
    var areAllStationsLoaded: Boolean = false
}

data class Track(
    val title: String,
    val location: String
)
