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
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.javokhirbekcoder.uzbekmusic.R
import com.javokhirbekcoder.uzbekmusic.interfaces.MusicServiceInterface
import com.javokhirbekcoder.uzbekmusic.models.MusicItem
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random


/*
Created by Javokhirbek on 22/03/2024 at 16:08
*/

@AndroidEntryPoint
class MusicService : Service(), MusicServiceInterface {

    companion object {

        private const val CHANNEL_ID = "MusicChannel"
        private const val NOTIFICATION_ID = 123
        private const val ACTION_PLAY_PAUSE = "action_play_pause"
        private const val ACTION_SKIP_NEXT = "action_skip_next"
        private const val ACTION_STOP = "action_stop"

        var playingMusicList = ArrayList<MusicItem>()
        var playingMusicIndex = 0;
        var currentMusic = MutableLiveData<MusicItem>()

        //  var musicPlayingInService = false
        var shuffle = false
        var repeat = true
        var mediaPlayer: MediaPlayer? = null
    }

    private val localBinder: IBinder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): MusicServiceInterface {
            Log.i("TAG", "getService: Sitting in local binder.")
            return this@MusicService
        }
    }


    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun initializeMusicPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                MediaPlayer(baseContext)
            } else {
                MediaPlayer()
            }
            startMusic()
            Log.d("TAG", " ---> Media player initialized!")
            mediaPlayer!!.setOnCompletionListener {
                if (playingMusicIndex + 1 == playingMusicList.size && !repeat) {
                    mediaPlayer!!.stop()
                    mediaPlayer!!.reset()
                    Log.d("TAG", "Music player stop, reset")
                } else
                    nextMusic()
            }
        }
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
        initializeMusicPlayer()
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
            .setSilent(true)
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
        playPauseMedia()
    }

    private fun playPauseMedia() {
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer!!.pause()
            Log.d("TAG", "pause, from service")
        } else {
            mediaPlayer!!.start()
            Log.d("TAG", "play, from service")
        }
    }

    private fun handleSkipNext() {
        nextMusic()
    }


    override fun nextMusic() {
        Log.d("TAG", "nextMusic Service")
        if (shuffle) {
            playingMusicIndex = Random.nextInt(playingMusicList.size)
            startMusic()
        } else {
            if (playingMusicIndex + 1 != playingMusicList.size) {
                playingMusicIndex += 1
                startMusic()
            } else {
                playingMusicIndex = 0
                startMusic()
            }
        }
    }

    override fun prevMusic() {
        Log.d("TAG", "prevMusic Service")
        if (shuffle) {
            playingMusicIndex = Random.nextInt(playingMusicList.size)
            startMusic()
        } else {
            if (playingMusicIndex - 1 >= 0) {
                playingMusicIndex -= 1
                startMusic()
            } else {
                playingMusicIndex = playingMusicList.size - 1
                startMusic()
            }
        }
    }

    private fun handleStop() {
        stopSelf()
    }

    override fun startMusic() {
        try {
            if (playingMusicList.isEmpty() || playingMusicList.size < 1) {
                Log.d("TAG", "Music list empty !!!")
            } else {
                //initializeMusicPlayer()
                val music = playingMusicList[playingMusicIndex]

                //val musicBasePath = getString(R.string.base_music_path) + music.music_url
                val musicBasePath =
                    "/sdcard/Android/data/com.javokhirbekcoder.uzbekmusic/files/" + music.music_url

                Log.d("TAG", "music url = $musicBasePath")

                val musicUri: Uri = Uri.parse(musicBasePath)

                currentMusic.postValue(music)

                mediaPlayer!!.reset()
                mediaPlayer!!.setDataSource(baseContext, musicUri)
                mediaPlayer!!.prepareAsync()
                mediaPlayer!!.setOnPreparedListener {
                    mediaPlayer!!.start()
                }

                updateNotification()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createNotificationChannel() {
        val name = "Music Channel"
        val descriptionText = "Channel for Music Player"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return localBinder
    }


    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
            Log.d("TAG", " <--- Media player release!")
        }
    }
}