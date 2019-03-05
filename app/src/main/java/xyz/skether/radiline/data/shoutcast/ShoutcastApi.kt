package xyz.skether.radiline.data.shoutcast

interface ShoutcastApi {

    /**
     * Get top stations with [limit] up to 500.
     */
    @Throws(ShoutcastError::class)
    suspend fun getTopStations(limit: Int, offset: Int): StationListResponse

    /**
     * Search stations which has query match in the following fields Station Name, Now Playing info, Genre.
     */
    @Throws(ShoutcastError::class)
    suspend fun searchStations(query: String, limit: Int, offset: Int): StationListResponse

    /**
     * Get stations by genre ID.
     */
    @Throws(ShoutcastError::class)
    suspend fun getStationsByGenreId(genreId: Long, limit: Int, offset: Int): StationListResponse

    /**
     * Get primary genres.
     */
    @Throws(ShoutcastError::class)
    suspend fun getPrimaryGenres(): GenreListResponse

    /**
     * Get secondary genres.
     */
    @Throws(ShoutcastError::class)
    suspend fun getSecondaryGenres(parentGenreId: Long): GenreListResponse

    /**
     * Get playlist for the station.
     */
    @Throws(ShoutcastError::class)
    suspend fun getPlaylist(stationId: Long, tuneInResource: String): PlaylistResponse

}
