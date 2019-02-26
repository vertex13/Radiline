package xyz.skether.radiline.domain

import xyz.skether.radiline.data.shoutcast.ShoutcastApi
import javax.inject.Inject

class StationsManager @Inject constructor(private val api: ShoutcastApi) {

    suspend fun getTopStations(limit: Int = 10, offset: Int = 0): List<Station> {
        val response = api.getTopStations(limit, offset)
        return response.stations.map(::stationFromResponse)
    }

    suspend fun searchStations(query: String, limit: Int = 10, offset: Int = 0): List<Station> {
        val response = api.searchStations(query, limit, offset)
        return response.stations.map(::stationFromResponse)
    }

    suspend fun getStationsByGenreId(genreId: Int, limit: Int = 10, offset: Int = 0): List<Station> {
        val response = api.getStationsByGenreId(genreId, limit, offset)
        return response.stations.map(::stationFromResponse)
    }

}
