package xyz.skether.radiline.domain.di

import android.content.Context
import dagger.Module
import dagger.Provides
import xyz.skether.radiline.data.db.AppDatabase

@Module
class DatabaseModule {

    @Provides
    fun provideDatabase(appContext: Context): AppDatabase {
        return AppDatabase.create(appContext)
    }

}
