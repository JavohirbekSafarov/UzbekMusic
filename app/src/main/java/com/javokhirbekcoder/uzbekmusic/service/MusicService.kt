package com.javokhirbekcoder.uzbekmusic.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.javokhirbekcoder.uzbekmusic.R
import com.javokhirbekcoder.uzbekmusic.models.MusicItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.random.Random

/*
Created by Javokhirbek on 22/03/2024 at 16:08
*/

@AndroidEntryPoint
class MusicService : Service() {

    companion object {
        private const val CHANNEL_ID = "MusicChannel"
        private const val NOTIFICATION_ID = 123
        private const val ACTION_PLAY_PAUSE = "action_play_pause"
        private const val ACTION_SKIP_NEXT = "action_skip_next"
        private const val ACTION_STOP = "action_stop"

        var playingMusicList = ArrayList<MusicItem>()
        var playingMusicIndex = 0;
        var musicPlayingInService = false
        var shuffle = false
        var repeat = true
    }

    @Inject
    lateinit var mediaPlayer: MediaPlayer

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, buildNotification())
        intent?.let {
            when (it.action) {
                ACTION_PLAY_PAUSE -> handlePlayPause()
                ACTION_SKIP_NEXT -> handleSkipNext()
                ACTION_STOP -> handleStop()
            }
        }
        return START_NOT_STICKY
    }

    private fun buildNotification(): Notification {
        val playPauseAction = NotificationCompat.Action(
            R.drawable.pause,
            "Play/Pause",
            getPendingIntent(ACTION_PLAY_PAUSE)
        )
//        val skipPreviousAction = NotificationCompat.Action(
//            R.drawable.skip_previous,
//            "Previous",
//            getPendingIntent(ACTION_SKIP_PREVIOUS)
//        )
        val skipNextAction = NotificationCompat.Action(
            R.drawable.skip_next,
            "Next",
            getPendingIntent(ACTION_SKIP_NEXT)
        )
        val stopAction = NotificationCompat.Action(
            R.drawable.music_note_3,
            "Stop",
            getPendingIntent(ACTION_STOP)
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.music_note_3)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(playingMusicList[playingMusicIndex].music_name)
            .addAction(playPauseAction)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .addAction(skipNextAction)
            .addAction(stopAction)
            .setBadgeIconType(R.drawable.music_note_3)
            .build()

        return notification
    }

    private fun getPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(
            this,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun updateNotification() {
        val notification = buildNotification()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }


    private fun handlePlayPause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
    }

    private fun handleSkipNext() {
        nextMusic()
        updateNotification()
    }

    private fun nextMusic() {

        if (shuffle) {
            playingMusicIndex = Random.nextInt(playingMusicList.size)
            startNextMusic()
        } else {
            if (playingMusicIndex + 1 != playingMusicList.size) {
                playingMusicIndex += 1
                startNextMusic()
            } else {
                if (repeat) {
                    playingMusicIndex = 0
                    startNextMusic()
                } else {
                    mediaPlayer.reset()
                }
            }
        }
    }

    private fun handleStop() {
        stopSelf()
    }

    private fun startNextMusic() {

        val music = playingMusicList[playingMusicIndex]

        val musicBasePath =
            getString(R.string.base_music_path) + music.music_url
        val musicUri: Uri = Uri.parse(musicBasePath)

        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(baseContext, musicUri)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                mediaPlayer.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun createNotificationChannel() {
        val name = "Music Channel"
        val descriptionText = "Channel for Music Player"
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }
}