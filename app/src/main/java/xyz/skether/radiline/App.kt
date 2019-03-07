package xyz.skether.radiline

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.leakcanary.LeakCanary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import xyz.skether.radiline.data.db.AppDatabase
import xyz.skether.radiline.domain.di.Injector
import xyz.skether.radiline.utils.logInfo
import java.util.concurrent.TimeUnit

class App : Application() {

    companion object {
        const val DEFAULT_NOTIFICATION_CHANNEL_ID = "default_notification_channel_id"
    }

    override fun onCreate() {
        super.onCreate()

        Injector.init(this)

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannels()
        }

        deleteOldDatabaseData(Injector.appComponent.appDatabase())

        logInfo("App is created")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannels() {
        val name = getString(R.string.default_notification_channel_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(DEFAULT_NOTIFICATION_CHANNEL_ID, name, importance)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun deleteOldDatabaseData(appDatabase: AppDatabase) {
        val freshDuration = TimeUnit.DAYS.toMillis(7)
        val freshTime = System.currentTimeMillis() - freshDuration

        GlobalScope.launch(Dispatchers.Default) {
            appDatabase.genreDao().deleteOld(freshTime)
            appDatabase.stationDao().deleteOld(freshTime)
        }
    }

}
