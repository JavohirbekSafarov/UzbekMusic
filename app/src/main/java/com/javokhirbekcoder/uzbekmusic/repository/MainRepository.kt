package com.javokhirbekcoder.uzbekmusic.repository

import android.media.MediaPlayer
import android.util.Log
import kotlinx.coroutines.async
import androidx.lifecycle.MutableLiveData
import com.javokhirbekcoder.uzbekmusic.models.Artists
import com.javokhirbekcoder.uzbekmusic.models.ArtistsItem
import com.javokhirbekcoder.uzbekmusic.models.Music
import com.javokhirbekcoder.uzbekmusic.models.MusicEntity
import com.javokhirbekcoder.uzbekmusic.models.MusicItem
import com.javokhirbekcoder.uzbekmusic.repository.api.RemoteDataSource
import com.javokhirbekcoder.uzbekmusic.repository.database.Dao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
Created by Javokhirbek on 11/03/2024 at 16:01
*/

class MainRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val dao: Dao
) {

    private val artistsLocal = MutableLiveData<List<ArtistsItem>>()
    private val musicsList = MutableLiveData<List<MusicEntity>>()

    init {
        getLocalArtists()
        getMusicsDatabaseLoad()
    }

    private fun getLocalArtists() {
        CoroutineScope(Dispatchers.IO).launch {
            //artistsLocal.postValue(localDataSource.getArtists())
            artistsLocal.postValue(dao.getAllArtists())
        }
    }

    fun getLocalArtistsList(): MutableLiveData<List<ArtistsItem>> = artistsLocal

    fun getOnlineArtists(): MutableLiveData<Artists> {
        return remoteDataSource.getArtists()
    }

    fun saveArtists(list: List<ArtistsItem>) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.saveAllArtists(list)
        }
    }

    fun deleteAllArtists() {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAll()
        }
    }

    fun saveMusic(musicFileName: String?) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val deferredResult: Deferred<MutableLiveData<List<MusicEntity>>> =
                    async { getAllMusicsOffline() }
                val list = deferredResult.await()

                for (music in list.value!!) {
                    if (music.music_name_path == musicFileName) {
                        music.is_have_offline = true
                        CoroutineScope(Dispatchers.IO).launch {
                            dao.updateMusic(music)
                        }
                        Log.d("TAG", "saveMusic saved offline correctly, name $musicFileName")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "saveMusic: ${e.printStackTrace()}", null)
        }
    }

    fun saveMusicDatabase(musicEntity: MusicEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.saveMusic(musicEntity)
        }
    }

    fun getMusicsDatabaseLoad() {
        CoroutineScope(Dispatchers.IO).launch {
            musicsList.postValue(dao.getAllMusics())
        }
    }

    fun getAllMusicsOffline(): MutableLiveData<List<MusicEntity>> {
        return musicsList
    }

    fun deleteMusic() {}
    fun deleteAllMusic() {}

    fun getMusicOnline(artistId: Int): MutableLiveData<Music> {
        return remoteDataSource.getMusics(artistId)
    }

    fun getAllMusics():MutableLiveData<Music>{
        return remoteDataSource.getAllMusics()
    }


    fun saveArtist(artistsItem: ArtistsItem) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.saveAllArtist(artistsItem)
        }
    }

}