package xyz.skether.radiline.domain.manager

import xyz.skether.radiline.data.db.AppDatabase
import xyz.skether.radiline.data.db.DBStation
import xyz.skether.radiline.data.db.DBUpdateInfo
import xyz.skether.radiline.data.shoutcast.ShoutcastApi
import xyz.skether.radiline.data.shoutcast.ShoutcastError
import xyz.skether.radiline.domain.*
import xyz.skether.radiline.utils.SuspendLazy
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StationsManager @Inject constructor(private val api: ShoutcastApi, private val db: AppDatabase) {

    private val freshDuration = TimeUnit.DAYS.toMillis(1)

    @Throws(ShoutcastError::class)
    suspend fun getTopStations(limit: Int = 10, offset: Int = 0, forceUpdate: Boolean = false): List<Station> {
        val methodName = "get_top_stations"
        val updateInfo = db.updateInfoDao().getByMethodName(methodName)
        val itemsNumber = limit + offset

        if (!forceUpdate
            && updateInfo != null
            && updateInfo.itemsNumber >= itemsNumber
            && updateInfo.updatedAt > freshTime()
        ) {
            // Return the local data.
            return db.stationDao().getTop(limit, offset).map(::stationFromDB)
        }

        val response = api.getTopStations(limit, offset)
        db.stationDao().insert(response.stations.map { stationResponseToDB(it, null) })
        db.updateInfoDao().insert(DBUpdateInfo(methodName, itemsNumber))
        return response.stations.map(::stationFromResponse)
    }

    @Throws(ShoutcastError::class)
    suspend fun searchStations(query: String, limit: Int = 10, offset: Int = 0): List<Station> {
        val response = api.searchStations(query, limit, offset)
        return response.stations.map(::stationFromResponse)
    }

    @Throws(ShoutcastError::class)
    suspend fun getStationsByGenreId(
        genreId: Long, limit: Int = 10, offset: Int = 0, forceUpdate: Boolean = false
    ): List<Station> {
        val lazyLocalData = SuspendLazy { db.stationDao().getByGenreId(genreId, limit, offset) }

        if (!forceUpdate && isDataFresh(freshTime(), lazyLocalData.getValue(), limit)) {
            return lazyLocalData.getValue().map(::stationFromDB)
        }

        return try {
            val externalData = api.getStationsByGenreId(genreId, limit, offset)
            db.tuneInDao().insert(externalData.tuneInList.map(::tuneResponseToDB))
            db.stationDao().insert(externalData.stations.map { stationResponseToDB(it, genreId) })
            externalData.stations.map(::stationFromResponse)
        } catch (e: ShoutcastError) {
            if (!forceUpdate && lazyLocalData.getValue().size == limit) {
                lazyLocalData.getValue().map(::stationFromDB)
            } else {
                throw e
            }
        }
    }

    private fun freshTime(): Long = System.currentTimeMillis() - freshDuration

    /**
     * The [dbStations] is not fresh if at least one station is updated before the [freshTime]
     * or the [dbStations]' size is not equals to [expectedSize].
     */
    private fun isDataFresh(freshTime: Long, dbStations: List<DBStation>, expectedSize: Int): Boolean {
        return dbStations.size == expectedSize && dbStations.find { freshTime > it.updatedAt } == null
    }

}
