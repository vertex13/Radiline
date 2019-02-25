package xyz.skether.radiline.data.shoutcast

interface ShoutcastApi {

    /**
     * Get top stations with [limit] up to 500.
     */
    suspend fun getTopStations(limit: Int, offset: Int): StationListResponse

    /**
     * Search stations which has query match in the following fields Station Name, Now Playing info, Genre.
     */
    suspend fun searchStations(query: String, limit: Int, offset: Int): StationListResponse

    /**
     * Get stations by genre ID.
     */
    suspend fun getStationsByGenreId(genreId: Int, limit: Int, offset: Int): StationListResponse

    /**
     * Get primary genres.
     */
    suspend fun getPrimaryGenres(): GenreListResponse

    /**
     * Get secondary genres.
     */
    suspend fun getSecondaryGenres(parentGenreId: Int): GenreListResponse

}
