package xyz.skether.radiline.domain.di

object Injector {

    val appComponent: AppComponent = DaggerAppComponent.builder()
        .shoutcastModule(ShoutcastModule())
        .build()

}
