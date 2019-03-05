package xyz.skether.radiline.domain

import xyz.skether.radiline.data.db.DBGenre
import xyz.skether.radiline.data.db.DBStation
import xyz.skether.radiline.data.db.DBTuneIn
import xyz.skether.radiline.data.shoutcast.GenreResponse
import xyz.skether.radiline.data.shoutcast.StationResponse
import xyz.skether.radiline.data.shoutcast.TrackResponse
import xyz.skether.radiline.data.shoutcast.TuneInResponse

fun stationFromResponse(resp: StationResponse) = Station(
    id = resp.id,
    name = resp.name,
    listeners = resp.numberListeners,
    bitrate = resp.bitRate
)

fun stationFromDB(dbStation: DBStation) = Station(
    id = dbStation.id,
    name = dbStation.name,
    listeners = dbStation.numberListeners,
    bitrate = dbStation.bitRate
)

fun stationResponseToDB(resp: StationResponse, genreId: Long) = DBStation(
    id = resp.id,
    genreId = genreId,
    name = resp.name,
    genreName = resp.genreName,
    mediaType = resp.mediaType,
    bitRate = resp.bitRate,
    currentTrack = resp.currentTrack,
    numberListeners = resp.numberListeners,
    logo = resp.logo
)

fun genreFromResponse(resp: GenreResponse) = Genre(
    id = resp.id,
    name = resp.name,
    hasSubGenres = resp.hasChildren
)

fun genreFromDB(dbGenre: DBGenre) = Genre(
    id = dbGenre.id,
    name = dbGenre.name,
    hasSubGenres = dbGenre.hasChildren
)

fun genreResponseToDB(resp: GenreResponse) = DBGenre(
    id = resp.id,
    parentId = resp.parentId,
    name = resp.name,
    hasChildren = resp.hasChildren
)

fun trackFromResponse(resp: TrackResponse) = Track(
    title = resp.title,
    location = resp.location
)

fun tuneResponseToDB(resp: TuneInResponse) = DBTuneIn(
    base = resp.base,
    resource = resp.resource
)
