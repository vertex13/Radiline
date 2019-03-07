package xyz.skether.radiline.domain.di

import android.content.Context
import dagger.Module
import dagger.Provides
import xyz.skether.radiline.data.db.AppDatabase
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(appContext: Context): AppDatabase {
        return AppDatabase.create(appContext)
    }

}
