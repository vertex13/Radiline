package xyz.skether.radiline.domain.manager

import xyz.skether.radiline.data.db.AppDatabase
import xyz.skether.radiline.data.shoutcast.ShoutcastApi
import xyz.skether.radiline.data.shoutcast.ShoutcastError
import xyz.skether.radiline.domain.Track
import xyz.skether.radiline.domain.trackFromResponse
import javax.inject.Inject

class PlaylistManager @Inject constructor(private val api: ShoutcastApi, private val db: AppDatabase) {

    private companion object {
        const val TUNE_IN_BASE = "base-xspf"
        const val DEFAULT_TUNE_IN_RESOURCE = "/sbin/tunein-station.xspf"
    }

    @Throws(ShoutcastError::class)
    suspend fun getStationTrack(stationId: Long): Track? {
        val tuneInResource = db.tuneInDao().getResource(TUNE_IN_BASE) ?: DEFAULT_TUNE_IN_RESOURCE
        val trackListResp = api.getPlaylist(stationId, tuneInResource)
        val trackResp = trackListResp.trackList.firstOrNull()
        return if (trackResp == null) null else trackFromResponse(trackResp)
    }

}
