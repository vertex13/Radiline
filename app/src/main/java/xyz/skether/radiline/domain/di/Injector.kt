package xyz.skether.radiline.domain.di

import xyz.skether.radiline.App

object Injector {

    lateinit var appComponent: AppComponent

    fun init(app: App) {
        if (!this::appComponent.isInitialized) {
            appComponent = DaggerAppComponent.builder()
                .contextModule(ContextModule(app))
                .build()
        }
    }

}
