package com.javokhirbekcoder.uzbekmusic.utils


import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.view.View
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths


/*
Created by Javokhirbek on 12/03/2024 at 12:13
*/

class MusicDownloader(private val context: Context) {

    private fun createFolderMusic() {
        val f = File(Environment.getExternalStorageDirectory(), "OnlineGroupMusic")
        try {
            Files.createDirectory(Paths.get(f.absolutePath))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun downloadMusic(url: String, fileName: String) {

        createFolderMusic()

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        //request.allowScanningByMediaScanner()
        request.setDestinationInExternalFilesDir(context, "/OnlineGroupMusic", fileName)
        //request.setNotificationVisibility(View.VISIBLE)
        //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, fileName)

        downloadManager.enqueue(request)
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
