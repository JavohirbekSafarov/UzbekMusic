package com.javokhirbekcoder.uzbekmusic.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.javokhirbekcoder.uzbekmusic.fragments.DownloadingFragment
import com.javokhirbekcoder.uzbekmusic.repository.MainRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/*
Created by Javokhirbek on 13/03/2024 at 16:32
*/

@AndroidEntryPoint
class DownloadCompleteReceiver : BroadcastReceiver() {

    @Inject
    lateinit var mainRepository: MainRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {

            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            //Toast.makeText(context, downloadId.toString(), Toast.LENGTH_SHORT).show()

            if (context != null) {
                val downloadManager =
                    context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val musicUri = downloadManager.getUriForDownloadedFile(downloadId)

                mainRepository.saveMusic(getMusicFileName(musicUri, context))

                //Toast.makeText(context, getMusicFileName(musicUri, context), Toast.LENGTH_SHORT).show()

            } else {
                Log.d("TAG", "Context null in downComListener")
            }

        }

    }

    fun getMusicFileName(musicUri: Uri, context: Context): String? {
        val contentResolver: ContentResolver = context.contentResolver

        val projection = arrayOf(MediaStore.Audio.Media.DISPLAY_NAME)

        var cursor: Cursor? = null
        try {
            cursor = contentResolver.query(musicUri, projection, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val fileNameIndex: Int =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                return cursor.getString(fileNameIndex)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return null
    }
}