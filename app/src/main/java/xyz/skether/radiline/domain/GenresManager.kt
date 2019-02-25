package xyz.skether.radiline.domain

import xyz.skether.radiline.data.shoutcast.ShoutcastApi
import javax.inject.Inject

class GenresManager @Inject constructor(private val api: ShoutcastApi) {

    suspend fun getPrimaryGenres(): List<Genre> {
        val response = api.getPrimaryGenres()
        return response.genres.map(::genreFromResponse)
    }

}
