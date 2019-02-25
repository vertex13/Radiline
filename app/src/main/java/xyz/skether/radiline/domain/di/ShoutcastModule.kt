package xyz.skether.radiline.domain.di

import dagger.Module
import dagger.Provides
import xyz.skether.radiline.data.shoutcast.ShoutcastApi
import xyz.skether.radiline.data.shoutcast.ShoutcastApiImpl

@Module
class ShoutcastModule {

    @Provides
    fun provideApi(): ShoutcastApi = ShoutcastApiImpl()

}
