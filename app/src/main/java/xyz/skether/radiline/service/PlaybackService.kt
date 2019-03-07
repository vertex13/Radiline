package xyz.skether.radiline.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.*
import xyz.skether.radiline.App
import xyz.skether.radiline.R
import xyz.skether.radiline.data.shoutcast.ShoutcastError
import xyz.skether.radiline.domain.Track
import xyz.skether.radiline.domain.di.Injector
import xyz.skether.radiline.domain.manager.PlaylistManager
import xyz.skether.radiline.ui.main.MainActivity
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PlaybackService : Service(), CoroutineScope {

    @Inject
    lateinit var playlistManager: PlaylistManager

    private val stationIdHistory: Deque<Long> = LinkedList()
    var currentTrack: Track? = null
        private set(value) {
            field = value
            listeners.forEach { it.onTrackChanged(value) }
        }
    var isPlaying: Boolean = false
        private set(value) {
            field = value
            listeners.forEach { it.onStateChanged(value) }
        }

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val binder = LocalBinder()

    private val listeners = mutableListOf<Listener>()
    private lateinit var exoPlayer: ExoPlayer

    override fun onCreate() {
        super.onCreate()
        Injector.appComponent.inject(this)
        exoPlayer = createExoPlayer()
    }

    override fun onDestroy() {
        exoPlayer.release()
        runBlocking {
            job.cancelAndJoin()
        }
        listeners.clear()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder = binder

    override fun onUnbind(intent: Intent?): Boolean {
        if (!isPlaying) {
            stopSelf()
        }
        return super.onUnbind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return START_NOT_STICKY
        when (intent.action) {
            ACTION_PLAY -> {
                val stationId = if (intent.hasExtra(PARAM_STATION_ID)) {
                    intent.getLongExtra(PARAM_STATION_ID, -1)
                } else {
                    null
                }
                playStation(stationId)
            }
            ACTION_STOP -> stopPlayback()
            ACTION_DESTROY -> destroyNotification()
        }
        return START_NOT_STICKY
    }

    /**
     * Turn on the station with the [stationId] or if the [stationId] is null turn on the previous station.
     */
    fun playStation(stationId: Long? = null) {
        val currentStationId = stationIdHistory.peek()
        val id = stationId ?: currentStationId ?: return
        if (id != currentStationId) {
            stationIdHistory.push(id)
        }
        isPlaying = true

        startForeground(NOTIFICATION_ID, createNotification())

        exoPlayer.stop(true)
        launch {
            val track = try {
                withContext(Dispatchers.Default) { playlistManager.getStationTrack(id) }
            } catch (e: ShoutcastError) {
                isPlaying = false
                updateNotification()
                onError(e)
                null
            }
            if (track != null) {
                currentTrack = track
                updateNotification()
                exoPlayer.prepare(createMediaSource(track.location))
                exoPlayer.playWhenReady = true
            }
        }
    }

    fun playPreviousStation() {
        if (stationIdHistory.size > 1) {
            stationIdHistory.poll()
            playStation()
        }
    }

    fun stopPlayback() {
        exoPlayer.stop(true)
        isPlaying = false
        stopForeground(false)
        updateNotification()
    }

    private fun destroyNotification() {
        exoPlayer.stop(true)
        isPlaying = false
        stopForeground(true)
        stopSelf()
    }

    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    private fun onError(exception: Exception) {
        listeners.forEach { it.onError(exception) }
    }

    private fun createExoPlayer(): ExoPlayer {
        val trackSelectionFactory = AdaptiveTrackSelection.Factory()
        return ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector(trackSelectionFactory))
    }

    private fun createMediaSource(location: String): MediaSource {
        val bandwidthMeter = DefaultBandwidthMeter()
        val dataSourceFactory = DefaultDataSourceFactory(
            this, Util.getUserAgent(this, packageName), bandwidthMeter
        )
        return ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(location))
    }

    private fun createNotification(): Notification {
        val mainActivityIntent = Intent(this, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(this, 0, mainActivityIntent, 0)

        val title = currentTrack?.title ?: getString(R.string.app_name)
        val actionDrawableId = if (isPlaying) {
            R.drawable.ic_stop_circle_filled_notification_24dp
        } else {
            R.drawable.ic_play_circle_filled_notification_24dp
        }
        val actionIntent = if (isPlaying) stopIntent(this) else playIntent(this)
        val actionPendingIntent = PendingIntent.getService(this, 0, actionIntent, 0)

        val destroyPendingIntent = PendingIntent.getService(this, 0, destroyIntent(this), 0)

        val layout = RemoteViews(packageName, R.layout.notification_playback)
        layout.setImageViewResource(R.id.play_button, actionDrawableId)
        layout.setTextViewText(R.id.title_view, title)
        layout.setImageViewResource(R.id.close_button, R.drawable.ic_close_notification_24dp)
        layout.setOnClickPendingIntent(R.id.play_button, actionPendingIntent)
        layout.setOnClickPendingIntent(R.id.close_button, destroyPendingIntent)

        return NotificationCompat.Builder(this, App.DEFAULT_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCustomContentView(layout)
            .setContentIntent(contentIntent)
            .build()
    }

    private fun updateNotification() {
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, createNotification())
    }

    interface Listener {
        fun onStateChanged(isPlaying: Boolean)

        fun onTrackChanged(track: Track?)

        fun onError(exception: Exception)
    }

    inner class LocalBinder : Binder() {
        val service = this@PlaybackService
    }

    companion object {

        private const val NOTIFICATION_ID = 3827

        private const val ACTION_PLAY = "action_play"
        private const val ACTION_STOP = "action_stop"
        private const val ACTION_DESTROY = "action_destroy"
        private const val PARAM_STATION_ID = "parameter_station_id"

        fun bindIntent(context: Context): Intent = Intent(context, PlaybackService::class.java)

        /**
         * Create an intent to turn on the station with the [stationId]
         * or if the [stationId] is null turn on the previous station.
         */
        fun playIntent(context: Context, stationId: Long? = null): Intent {
            return Intent(context, PlaybackService::class.java).apply {
                action = ACTION_PLAY
                if (stationId != null) {
                    putExtra(PARAM_STATION_ID, stationId)
                }
            }
        }

        fun stopIntent(context: Context): Intent {
            return Intent(context, PlaybackService::class.java).apply {
                action = ACTION_STOP
            }
        }

        fun destroyIntent(context: Context): Intent {
            return Intent(context, PlaybackService::class.java).apply {
                action = ACTION_DESTROY
            }
        }

    }

}
