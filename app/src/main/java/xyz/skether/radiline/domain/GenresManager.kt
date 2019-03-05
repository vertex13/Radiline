package xyz.skether.radiline.domain

import xyz.skether.radiline.data.shoutcast.ShoutcastApi
import javax.inject.Inject

class GenresManager @Inject constructor(private val api: ShoutcastApi) {

    suspend fun getGenres(parentGenreId: Long? = null): List<Genre> {
        return if (parentGenreId == null) {
            getPrimaryGenres()
        } else {
            getSecondaryGenres(parentGenreId)
        }
    }

    private suspend fun getPrimaryGenres(): List<Genre> {
        val response = api.getPrimaryGenres()
        return response.genres.map(::genreFromResponse)
    }

    private suspend fun getSecondaryGenres(parentGenreId: Long): List<Genre> {
        val response = api.getSecondaryGenres(parentGenreId)
        return response.genres.map(::genreFromResponse)
    }

}
