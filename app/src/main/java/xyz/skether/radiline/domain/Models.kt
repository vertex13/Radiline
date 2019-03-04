package xyz.skether.radiline.domain

import xyz.skether.radiline.data.shoutcast.GenreResponse
import xyz.skether.radiline.data.shoutcast.StationResponse
import xyz.skether.radiline.data.shoutcast.TrackResponse

data class Station(
    val id: Int,
    val name: String,
    val listeners: Int = 1234,
    val bitrate: Int = 128
) {
    var genre: Genre? = null
}

data class Genre(
    val id: Int,
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

fun stationFromResponse(resp: StationResponse) = Station(
    id = resp.id,
    name = resp.name,
    listeners = resp.numberListeners,
    bitrate = resp.bitRate
)

fun genreFromResponse(resp: GenreResponse) = Genre(
    id = resp.id,
    name = resp.name,
    hasSubGenres = resp.hasChildren
)

fun trackFromResponse(resp: TrackResponse) = Track(
    title = resp.title,
    location = resp.location
)