package xyz.skether.radiline.domain

import xyz.skether.radiline.data.shoutcast.ShoutcastApi
import javax.inject.Inject

class StationsManager @Inject constructor(private val api: ShoutcastApi) {

    suspend fun getTopStations(page: Int = 0, limit: Int = 10): List<Station> {
        val response = api.getTopStations(limit, page * limit)
        return response.stations.map(::stationFromResponse)
    }

    suspend fun searchStations(query: String, page: Int = 0, limit: Int = 10): List<Station> {
        val response = api.searchStations(query, limit, page * limit)
        return response.stations.map(::stationFromResponse)
    }

}
