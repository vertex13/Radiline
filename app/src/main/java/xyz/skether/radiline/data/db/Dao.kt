package xyz.skether.radiline.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stations: List<DBStation>)

    @Query(
        "SELECT * FROM station WHERE genreId = :genreId" +
                " ORDER BY numberListeners DESC LIMIT :limit OFFSET :offset"
    )
    suspend fun getByGenreId(genreId: Long, limit: Int, offset: Int): List<DBStation>

    @Query("SELECT * FROM station ORDER BY numberListeners DESC LIMIT :limit OFFSET :offset")
    suspend fun getTop(limit: Int, offset: Int): List<DBStation>

    @Query("DELETE FROM station WHERE updatedAt < :time")
    suspend fun deleteOld(time: Long)

}

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(genres: List<DBGenre>)

    @Query("SELECT * FROM genre WHERE parentId IS NULL ORDER BY name ASC")
    suspend fun getPrimary(): List<DBGenre>

    @Query("SELECT * FROM genre WHERE parentId = :parentId ORDER BY name ASC")
    suspend fun getSecondary(parentId: Long): List<DBGenre>

    @Query("DELETE FROM genre WHERE updatedAt < :time")
    suspend fun deleteOld(time: Long)

}

@Dao
interface TuneInDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tuneIns: List<DBTuneIn>)

    @Query("SELECT resource FROM tune_in WHERE base = :base")
    suspend fun getResource(base: String): String?

}

@Dao
interface UpdateInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(info: DBUpdateInfo)

    @Query("SELECT * FROM update_info WHERE methodName = :methodName")
    suspend fun getByMethodName(methodName: String): DBUpdateInfo?

}
