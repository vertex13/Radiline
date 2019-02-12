package xyz.skether.radiline

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        logInfo("App is created")
    }

}
