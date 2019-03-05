package xyz.skether.radiline.data.shoutcast

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.await
import xyz.skether.radiline.data.shoutcast.ShoutcastApiRouting.ResponseFormat.XML

class ShoutcastApiImpl : ShoutcastApi {

    override suspend fun getTopStations(limit: Int, offset: Int): StationListResponse {
        return Fuel.request(ShoutcastApiRouting.GetTopStations(limit, offset))
            .await(StationListResponse.LegacyDeserializer)
    }

    override suspend fun searchStations(query: String, limit: Int, offset: Int): StationListResponse {
        return Fuel.request(ShoutcastApiRouting.SearchStations(query, limit, offset))
            .await(StationListResponse.LegacyDeserializer)
    }

    override suspend fun getStationsByGenreId(genreId: Long, limit: Int, offset: Int): StationListResponse {
        return Fuel.request(ShoutcastApiRouting.GetStationsByGenreId(genreId, limit, offset, XML))
            .await(StationListResponse.Deserializer)
    }

    override suspend fun getPrimaryGenres(): GenreListResponse {
        return Fuel.request(ShoutcastApiRouting.GetPrimaryGenres(XML))
            .await(GenreListResponse.Deserializer)
    }

    override suspend fun getSecondaryGenres(parentGenreId: Long): GenreListResponse {
        return Fuel.request(ShoutcastApiRouting.GetSecondaryGenres(parentGenreId, XML))
            .await(GenreListResponse.Deserializer)
    }

    override suspend fun getPlaylist(stationId: Long, tuneIn: String): PlaylistResponse {
        return Fuel.request(ShoutcastApiRouting.GetPlaylist(stationId, tuneIn))
            .await(PlaylistResponse.Deserializer)
    }

}
