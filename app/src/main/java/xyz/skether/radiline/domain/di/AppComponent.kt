package xyz.skether.radiline.domain.di

import dagger.Component
import xyz.skether.radiline.data.shoutcast.ShoutcastApi
import xyz.skether.radiline.viewmodel.GenresViewModel
import xyz.skether.radiline.viewmodel.SearchViewModel
import xyz.skether.radiline.viewmodel.TopStationsViewModel

@Component(modules = [ShoutcastModule::class])
interface AppComponent {

    fun shoutcastApi(): ShoutcastApi

    // view models
    fun inject(target: GenresViewModel)

    fun inject(target: TopStationsViewModel)

    fun inject(target: SearchViewModel)

}
