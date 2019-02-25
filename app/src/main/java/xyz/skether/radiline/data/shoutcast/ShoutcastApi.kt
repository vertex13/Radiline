package xyz.skether.radiline.data.shoutcast

interface ShoutcastApi {

    /**
     * Get top stations with [limit] up to 500.
     */
    suspend fun getTopStations(limit: Int): StationListResponse

}
