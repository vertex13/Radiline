package xyz.skether.radiline.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DBStation::class, DBGenre::class, DBTuneIn::class, DBUpdateInfo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun stationDao(): StationDao

    abstract fun genreDao(): GenreDao

    abstract fun tuneInDao(): TuneInDao

    abstract fun updateInfoDao(): UpdateInfoDao

    companion object {

        fun create(appContext: Context): AppDatabase {
            return Room.databaseBuilder(appContext, AppDatabase::class.java, "radiline_db").build()
        }

    }

}
