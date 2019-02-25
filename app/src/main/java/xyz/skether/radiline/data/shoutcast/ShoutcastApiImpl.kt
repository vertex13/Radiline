package xyz.skether.radiline.data.shoutcast

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.await

class ShoutcastApiImpl : ShoutcastApi {

    override suspend fun getTopStations(limit: Int): StationListResponse {
        return Fuel.request(ShoutcastApiRouting.GetTopStations(limit))
            .await(StationListResponse.Deserializer)
    }

}
