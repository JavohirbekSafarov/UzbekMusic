package com.javokhirbekcoder.uzbekmusic

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatDelegate
import com.javokhirbekcoder.uzbekmusic.databinding.ActivityMainBinding
import com.javokhirbekcoder.uzbekmusic.utils.MusicDownloader
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer:MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


       /* {
            "id": 1,
            "artist_id": "1",
            "artist": "Jaloliddin Ahmadaliyev",
            "music_name": "Sog'indim",
            "duration": "04:55",
            "size": "5.1",
            "music_url": "https://nevomusic.net/download.php?idfile=track-014307.mp3",
            "music_img": "https://is4-ssl.mzstatic.com/image/thumb/Music126/v4/75/b7/db/75b7dbca-f72a-d703-8229-cd0252c6a677/8720693724435.png/1200x1200bf-60.jpg"
        }*/

        // yuklash
        // MusicDownloader(this).downloadMusic("https://nevomusic.net/download.php?idfile=track-014307.mp3", "Sog'indim.mp3")
        // ishladi

        //eshitish
        mediaPlayer = MediaPlayer()


        val musicUri: Uri = Uri.parse("/sdcard/Android/data/com.javokhirbekcoder.uzbekmusic/files/OnlineGroupMusic/Sog'indim.mp3")

        try {
            mediaPlayer!!.setDataSource(applicationContext, musicUri)
            mediaPlayer!!.prepareAsync()
            mediaPlayer!!.setOnPreparedListener {
                mediaPlayer!!.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getMusicFiles(context:Context): List<Uri> {
        val musicList = mutableListOf<Uri>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DATA
        )

        val selection = "${MediaStore.Audio.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf("%/data/com.javokhirbekcoder.uzberkmusic%")

        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"

        context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val contentUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id.toString())
                musicList.add(contentUri)
            }
        }

        return musicList
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer!!.stop()
            mediaPlayer = null
        }
    }
}