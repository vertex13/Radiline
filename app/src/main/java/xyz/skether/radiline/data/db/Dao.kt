package xyz.skether.radiline.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg stations: DBStation)

    @Query("SELECT * FROM station WHERE id = :id")
    suspend fun getById(id: Long): DBStation?

    @Query(
        "SELECT * FROM station WHERE genreId = :genreId" +
                " ORDER BY numberListeners DESC LIMIT :limit OFFSET :offset"
    )
    suspend fun getByGenreId(genreId: Long, limit: Int, offset: Int): List<DBStation>

}

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg genres: DBGenre)

    @Query("SELECT * FROM genre WHERE id = :id")
    suspend fun getById(id: Long): DBGenre?

    @Query("SELECT * FROM genre WHERE parentId = NULL ORDER BY name ASC")
    suspend fun getPrimary(): List<DBGenre>

    @Query("SELECT * FROM genre WHERE parentId = :parentId ORDER BY name ASC")
    suspend fun getSecondary(parentId: Long): List<DBGenre>

}

@Dao
interface TuneInDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg tuneIns: DBTuneIn)

    @Query("SELECT resource FROM tune_in WHERE base = :base")
    suspend fun getResource(base: String): String?

}
