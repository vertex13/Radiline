package xyz.skether.radiline.domain.di

import android.content.Context
import dagger.Module
import dagger.Provides
import xyz.skether.radiline.App

@Module
class ContextModule(private val app: App) {

    @Provides
    fun provideApp(): App = app

    @Provides
    fun provideApplicationContext(): Context = app.applicationContext

}
