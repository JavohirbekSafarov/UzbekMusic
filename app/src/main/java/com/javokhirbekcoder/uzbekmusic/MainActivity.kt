package com.javokhirbekcoder.uzbekmusic

import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.javokhirbekcoder.uzbekmusic.databinding.ActivityMainBinding
import com.javokhirbekcoder.uzbekmusic.interfaces.MusicServiceInterface
import com.javokhirbekcoder.uzbekmusic.repository.api.RemoteDataSource
import com.javokhirbekcoder.uzbekmusic.service.MusicService
import com.javokhirbekcoder.uzbekmusic.utils.MusicDownloader
import com.javokhirbekcoder.uzbekmusic.utils.PermissionRequester
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
open class MainActivity : AppCompatActivity(), MusicServiceInterface {

    private lateinit var binding: ActivityMainBinding

//    @Inject
//    lateinit var mediaPlayer: MediaPlayer

    @Inject
    lateinit var musicDownloader: MusicDownloader

    @Inject
    lateinit var remoteDataSource: RemoteDataSource

    //   var playingMusicList = ArrayList<MusicItem>()
    //  var playingMusicIndex = 0;

    companion object {
        var yandexAd = false
    }

//    private val connection = object : ServiceConnection {
//        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//            isServiceBound = true
//            Log.d("TAG", "onServiceConnected")
//        }
//
//        override fun onServiceDisconnected(name: ComponentName?) {
//            isServiceBound = false
//            Log.d("TAG", "onServiceDisconnected")
//        }
//    }

    private val multiplePermissionId = 14
    private val multiplePermissionNameList =
        if (Build.VERSION.SDK_INT >= 33) {
            arrayListOf(
                android.Manifest.permission.READ_MEDIA_AUDIO
            )
        } else {
            arrayListOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

      //  window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        remoteDataSource.getStates().observe(this) {
            yandexAd = it.yandex_ads
            Log.d(
                "TAG", "States:\n\tAPP VERSION: ${it.version}\n" +
                        "\tYANDEX AD: ${it.yandex_ads}\n" +
                        "\tADMOB AD: ${it.google_ads}"
            )
        }

        // hideFolder()
        //Log.d("TAG", "onCreate: ${this.filesDir}")

        if (checkMultiplePermission()) {
            Log.d("TAG", "All permissions Granted!")
        } else {
            Log.d("TAG", "All permissions Denied!")
        }
        window.addFlags(FLAG_KEEP_SCREEN_ON)
    }

    fun startService() {
        val serviceIntent = Intent(this, MusicService::class.java)
        startService(serviceIntent)
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        Log.d("TAG", "Start service")
    }

    override fun onResume() {
        super.onResume()
        if (MusicService.mediaPlayer != null){
            val serviceIntent = Intent(this, MusicService::class.java)
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private fun checkMultiplePermission(): Boolean {
        val listPermissions = arrayListOf<String>()

        for (permission in multiplePermissionNameList) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                listPermissions.add(permission)
            }
        }
        if (listPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissions.toTypedArray(),
                multiplePermissionId
            )
            return false
        }
        return true
    }

    /*  private fun hideFolder() {

          val oldFolder = File("/storage/emulated/0/Music/OnlineGroupMusic")
          val newFolder = File("/storage/emulated/0/Music/.OnlineGroupMusic")

          if (newFolder.exists()) {
  //            val files = newFolder.listFiles()
  //
  //            files?.forEach { file ->
  //                file.delete()
  //            }

              val path = "/storage/emulated/0/Music/.OnlineGroupMusic"
              val deleteCmd = "rm -r $path"
              val runtime = Runtime.getRuntime()
              try {
                  runtime.exec(deleteCmd)
              } catch (e: IOException) {
                  e.printStackTrace()
                  Log.d("TAG", "Error -> ${e.printStackTrace()}")
              }

  //            if (newFolder.deleteRecursively()) {
  //                Log.d("TAG", "Delete Old folder")
  //            } else {
  //                Log.d("TAG", "Fail Delete Old folder")
  //            }
          }

          if (oldFolder.exists()) {
              val renamed = oldFolder.renameTo(newFolder)
              if (renamed) {
                  Log.d("TAG", "Folder renamed successfully.")
              } else {
                  Log.d("TAG", "Failed to rename folder.")
              }
          } else {
              Log.d("TAG", "Folder does not exist.")
          }
      }
  */
    /*   fun getMusicFiles(context:Context): List<Uri> {
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
   */


    override fun onPause() {
        super.onPause()
        window.clearFlags(FLAG_KEEP_SCREEN_ON)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == multiplePermissionId) {
            if (grantResults.isNotEmpty()) {
                var isGrand = true
                for (element in grantResults) {
                    if (element == PackageManager.PERMISSION_DENIED) {
                        isGrand = false
                    }
                }
                if (isGrand) {
                    // all permission granted
                    // Toast.makeText(this, "all Permission Granted", Toast.LENGTH_SHORT).show()
                } else {
                    var someDenied = false
                    for (permission in permissions) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                permission
                            )
                        ) {
                            if (ActivityCompat.checkSelfPermission(
                                    this,
                                    permission
                                ) == PackageManager.PERMISSION_DENIED
                            ) {
                                someDenied = true
                            }
                        }
                    }
                    if (someDenied) {
                        PermissionRequester().openAppSettings(this)
                    } else {
                        PermissionRequester().warningPermissionDialog(
                            this
                        ) { _, p1 ->
                            when (p1) {
                                DialogInterface.BUTTON_POSITIVE -> {
                                    checkMultiplePermission()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun nextMusic() {
        Log.d("TAG", "nextMusic: Activity")
        musicService?.nextMusic()
    }

    override fun prevMusic() {
        Log.d("TAG", "prevMusic: Activity")
        musicService?.prevMusic()
    }

    override fun startMusic() {
        Log.d("TAG", "startMusic: Activity")
        musicService?.startMusic()
    }


    protected var musicService: MusicServiceInterface? = null

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            musicService = (iBinder as MusicService.LocalBinder).getService()
            Log.d("TAG", "onServiceConnected")
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            musicService = null
        }
    }
}