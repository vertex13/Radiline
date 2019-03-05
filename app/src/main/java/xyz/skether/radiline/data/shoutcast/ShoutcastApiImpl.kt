package xyz.skether.radiline.data.shoutcast

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Deserializable
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.await
import xyz.skether.radiline.data.shoutcast.ShoutcastApiRouting.ResponseFormat.XML

class ShoutcastApiImpl : ShoutcastApi {

    @Throws(ShoutcastError::class)
    override suspend fun getTopStations(limit: Int, offset: Int): StationListResponse {
        return performRequest(
            Fuel.request(ShoutcastApiRouting.GetTopStations(limit, offset)),
            StationListResponse.LegacyDeserializer
        )
    }

    @Throws(ShoutcastError::class)
    override suspend fun searchStations(query: String, limit: Int, offset: Int): StationListResponse {
        return performRequest(
            Fuel.request(ShoutcastApiRouting.SearchStations(query, limit, offset)),
            StationListResponse.LegacyDeserializer
        )
    }

    @Throws(ShoutcastError::class)
    override suspend fun getStationsByGenreId(genreId: Long, limit: Int, offset: Int): StationListResponse {
        return performRequest(
            Fuel.request(ShoutcastApiRouting.GetStationsByGenreId(genreId, limit, offset, XML)),
            StationListResponse.Deserializer
        )
    }

    @Throws(ShoutcastError::class)
    override suspend fun getPrimaryGenres(): GenreListResponse {
        return performRequest(
            Fuel.request(ShoutcastApiRouting.GetPrimaryGenres(XML)),
            GenreListResponse.Deserializer
        )
    }

    @Throws(ShoutcastError::class)
    override suspend fun getSecondaryGenres(parentGenreId: Long): GenreListResponse {
        return performRequest(
            Fuel.request(ShoutcastApiRouting.GetSecondaryGenres(parentGenreId, XML)),
            GenreListResponse.Deserializer
        )
    }

    @Throws(ShoutcastError::class)
    override suspend fun getPlaylist(stationId: Long, tuneInResource: String): PlaylistResponse {
        return performRequest(
            Fuel.request(ShoutcastApiRouting.GetPlaylist(stationId, tuneInResource)),
            PlaylistResponse.Deserializer
        )
    }

    @Throws(ShoutcastError::class)
    private suspend fun <T : Any, U : Deserializable<T>> performRequest(request: Request, deserializable: U): T {
        return try {
            request.await(deserializable)
        } catch (e: FuelError) {
            throw ShoutcastError(e)
        }
    }

}
