package xyz.skether.radiline.data.shoutcast

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.await

class ShoutcastApiImpl : ShoutcastApi {

    override suspend fun getTopStations(limit: Int, offset: Int): StationListResponse {
        return Fuel.request(ShoutcastApiRouting.GetTopStations(limit, offset))
            .await(StationListResponse.Deserializer)
    }

    override suspend fun searchStations(query: String, limit: Int, offset: Int): StationListResponse {
        return Fuel.request(ShoutcastApiRouting.SearchStations(query, limit, offset))
            .await(StationListResponse.Deserializer)
    }

    override suspend fun getPrimaryGenres(): GenreListResponse {
        return Fuel.request(ShoutcastApiRouting.GetPrimaryGenres(ShoutcastApiRouting.ResponseFormat.XML))
            .await(GenreListResponse.Deserializer)
    }

}
