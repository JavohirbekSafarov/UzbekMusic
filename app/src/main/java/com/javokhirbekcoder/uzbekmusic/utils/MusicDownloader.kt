package com.javokhirbekcoder.uzbekmusic.utils


import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.javokhirbekcoder.uzbekmusic.R
import com.javokhirbekcoder.uzbekmusic.models.MusicEntity
import com.javokhirbekcoder.uzbekmusic.models.MusicItem
import com.javokhirbekcoder.uzbekmusic.repository.MainRepository
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject


/*
Created by Javokhirbek on 12/03/2024 at 12:13
*/

class MusicDownloader @Inject constructor(
    private val context: Context,
    private val mainRepository: MainRepository
) {

/*    private fun createFolderMusic() {
        val f = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
            "OnlineGroupMusic"
        )
        Log.d("TAG", "createFolderMusic: ${Paths.get(f.absolutePath)}")
        try {
            Files.createDirectory(Paths.get(f.absolutePath))
            //Files.createDirectory(Paths.get(context.getString(R.string.base_music_path2)))
            //Log.d("TAG", "createFolderMusic: ${Paths.get(context.getString(R.string.base_music_path2))}")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }*/

    fun downloadMusic(musicItem: MusicItem) {

        // todo bosh joylarni qirqish kk


        val fileName = removeSpecialCharacters(musicItem.music_name) + ".mp3"

        //createFolderMusic()

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(musicItem.music_url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalFilesDir(context, null, fileName)

//        request.setDestinationInExternalPublicDir(
//            Environment.DIRECTORY_MUSIC,
//            "OnlineGroupMusic/$fileName"
//        )

       // request.setDestinationUri()

        //request.setDestinationUri(Uri.parse(context.getString(R.string.base_music_path2) + fileName))
        downloadManager.enqueue(request)

        mainRepository.saveMusicDatabase(
            MusicEntity(
                musicItem.id,
                musicItem.artist,
                musicItem.duration,
                musicItem.music_img,
                musicItem.music_name,
                fileName,
                false
            )
        )
    }

    fun removeSpecialCharacters(inputString: String): String {
        return inputString.replace("[/'\"`/?\\\\|:;,.<>()]".toRegex(), "")
    }

//
//    fun downloadMusic(url: String, fileName: String) {
//        val client = OkHttpClient()
//        val request = Request.Builder()
//            .url(url)
//            .build()
//
//        client.newCall(request).execute().use { response ->
//            if (!response.isSuccessful) throw Exception("Failed to download: $response")
//
//            val inputStream = response.body?.byteStream()
//            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName)
//
//            inputStream?.use { input ->
//                FileOutputStream(file).use { output ->
//                    input.copyTo(output)
//                }
//            }
//        }
//    }


}
