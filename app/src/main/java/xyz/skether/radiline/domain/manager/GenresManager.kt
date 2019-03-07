package xyz.skether.radiline.domain.manager

import xyz.skether.radiline.data.db.AppDatabase
import xyz.skether.radiline.data.db.DBGenre
import xyz.skether.radiline.data.shoutcast.ShoutcastApi
import xyz.skether.radiline.data.shoutcast.ShoutcastError
import xyz.skether.radiline.domain.Genre
import xyz.skether.radiline.domain.genreFromDB
import xyz.skether.radiline.domain.genreFromResponse
import xyz.skether.radiline.domain.genreResponseToDB
import xyz.skether.radiline.utils.SuspendLazy
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GenresManager @Inject constructor(private val api: ShoutcastApi, private val db: AppDatabase) {

    private val freshDuration = TimeUnit.DAYS.toMillis(1)

    @Throws(ShoutcastError::class)
    suspend fun getPrimaryGenres(forceUpdate: Boolean = false): List<Genre> {
        val lazyLocalData = SuspendLazy { db.genreDao().getPrimary() }

        if (!forceUpdate && isDataFresh(freshTime(), lazyLocalData.getValue())) {
            return lazyLocalData.getValue().map(::genreFromDB)
        }

        return try {
            val externalData = api.getPrimaryGenres()
            db.genreDao().insert(externalData.genres.map(::genreResponseToDB))
            externalData.genres.map(::genreFromResponse)
        } catch (e: ShoutcastError) {
            if (!forceUpdate && lazyLocalData.getValue().isNotEmpty()) {
                lazyLocalData.getValue().map(::genreFromDB)
            } else {
                throw e
            }
        }
    }

    @Throws(ShoutcastError::class)
    suspend fun getSecondaryGenres(parentGenreId: Long, forceUpdate: Boolean = false): List<Genre> {
        val lazyLocalData = SuspendLazy { db.genreDao().getSecondary(parentGenreId) }

        if (!forceUpdate && isDataFresh(freshTime(), lazyLocalData.getValue())) {
            return lazyLocalData.getValue().map(::genreFromDB)
        }

        return try {
            val externalData = api.getSecondaryGenres(parentGenreId)
            db.genreDao().insert(externalData.genres.map(::genreResponseToDB))
            externalData.genres.map(::genreFromResponse)
        } catch (e: Throwable) {
            if (!forceUpdate && lazyLocalData.getValue().isNotEmpty()) {
                lazyLocalData.getValue().map(::genreFromDB)
            } else {
                throw e
            }
        }
    }

    private fun freshTime(): Long = System.currentTimeMillis() - freshDuration

    /**
     * The [dbGenres] is not fresh if at least one genre is updated before the [freshTime]
     * or the [dbGenres] is empty.
     */
    private fun isDataFresh(freshTime: Long, dbGenres: List<DBGenre>): Boolean {
        return dbGenres.isNotEmpty() && dbGenres.find { freshTime > it.updatedAt } == null
    }

}
