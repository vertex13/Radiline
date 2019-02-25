package xyz.skether.radiline.domain

import xyz.skether.radiline.data.shoutcast.ShoutcastApi
import javax.inject.Inject

class StationsManager @Inject constructor(private val api: ShoutcastApi) {

    suspend fun getTopStations(limit: Int): List<Station> {
        val response = api.getTopStations(limit)
        return response.stations.map(::stationFromResponse)
    }

}
