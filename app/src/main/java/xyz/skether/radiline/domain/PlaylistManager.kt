package xyz.skether.radiline.domain

import xyz.skether.radiline.data.shoutcast.ShoutcastApi
import javax.inject.Inject

class PlaylistManager @Inject constructor(private val api: ShoutcastApi) {

    suspend fun getStationTrack(stationId: Long): Track? {
        // todo load tuneIn from DB
        val trackListResp = api.getPlaylist(stationId, "/sbin/tunein-station.xspf")
        val trackResp = trackListResp.trackList.firstOrNull()
        return if (trackResp == null) null else trackFromResponse(trackResp)
    }

}
