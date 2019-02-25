package xyz.skether.radiline.data.shoutcast

import com.github.kittinunf.fuel.core.HeaderValues
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.util.FuelRouting
import xyz.skether.radiline.BuildConfig

sealed class ShoutcastApiRouting : FuelRouting {

    class GetTopStations(val limit: Int) : ShoutcastApiRouting()

    override val basePath: String = "http://api.shoutcast.com"
    private val keyParam = "k" to BuildConfig.ShoutcastDevId

    override val method: Method
        get() = when (this) {
            is GetTopStations -> Method.GET
        }

    override val params: Parameters?
        get() = when (this) {
            is GetTopStations -> defaultParamsWith("limit" to limit)
        }

    override val path: String
        get() = when (this) {
            is GetTopStations -> "legacy/Top500"
        }

    override val body: String? = null

    override val bytes: ByteArray? = null

    override val headers: Map<String, HeaderValues>? = null

    private fun defaultParamsWith(vararg params: Pair<String, Any?>): Parameters {
        val list = mutableListOf<Pair<String, Any?>>(keyParam)
        params.forEach { list.add(it) }
        return list
    }

}
