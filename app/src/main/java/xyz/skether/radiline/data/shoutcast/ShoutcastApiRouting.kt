package xyz.skether.radiline.data.shoutcast

import com.github.kittinunf.fuel.core.HeaderValues
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.util.FuelRouting
import xyz.skether.radiline.BuildConfig

sealed class ShoutcastApiRouting : FuelRouting {

    enum class ResponseFormat { XML, JSON, RSS }

    interface Paginated {
        val limit: Int
        val offset: Int
    }

    class GetTopStations(
        override val limit: Int,
        override val offset: Int
    ) : ShoutcastApiRouting(), Paginated

    class SearchStations(
        val query: String,
        override val limit: Int,
        override val offset: Int
    ) : ShoutcastApiRouting(), Paginated

    class GetStationsByGenreId(
        val genreId: Long,
        override val limit: Int,
        override val offset: Int,
        val responseFormat: ResponseFormat
    ) : ShoutcastApiRouting(), Paginated

    class GetPrimaryGenres(
        val responseFormat: ResponseFormat
    ) : ShoutcastApiRouting()

    class GetSecondaryGenres(
        val parentGenreId: Long,
        val responseFormat: ResponseFormat
    ) : ShoutcastApiRouting()

    class GetPlaylist(
        val stationId: Long,
        val tuneIn: String
    ) : ShoutcastApiRouting()

    private val keyParam = "k" to BuildConfig.ShoutcastDevId

    override val basePath: String
        get() = when (this) {
            is GetPlaylist -> "http://yp.shoutcast.com"
            else -> "http://api.shoutcast.com"
        }

    override val method: Method = Method.GET

    override val params: Parameters?
        get() = when (this) {
            is GetTopStations -> defaultParamsWith()
            is SearchStations -> defaultParamsWith("search" to query)
            is GetStationsByGenreId -> defaultParamsWith("genre_id" to genreId, "f" to responseFormat)
            is GetPrimaryGenres -> defaultParamsWith("f" to responseFormat)
            is GetSecondaryGenres -> defaultParamsWith("parentid" to parentGenreId, "f" to responseFormat)
            is GetPlaylist -> listOf("id" to stationId)
        }

    override val path: String
        get() = when (this) {
            is GetTopStations -> "legacy/Top500"
            is SearchStations -> "legacy/stationsearch"
            is GetStationsByGenreId -> "station/advancedsearch"
            is GetPrimaryGenres -> "genre/primary"
            is GetSecondaryGenres -> "genre/secondary"
            is GetPlaylist -> tuneIn
        }

    override val body: String? = null

    override val bytes: ByteArray? = null

    override val headers: Map<String, HeaderValues>? = null

    private fun defaultParamsWith(vararg params: Pair<String, Any?>): Parameters {
        val list = mutableListOf<Pair<String, Any?>>(keyParam)
        if (this is Paginated) {
            list.add("limit" to "$offset,$limit")
        }
        params.forEach { list.add(it) }
        return list
    }

}
