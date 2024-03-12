package com.javokhirbekcoder.uzbekmusic.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.javokhirbekcoder.uzbekmusic.models.Artists
import com.javokhirbekcoder.uzbekmusic.models.ArtistsItem
import com.javokhirbekcoder.uzbekmusic.repository.api.RemoteDataSource
import com.javokhirbekcoder.uzbekmusic.repository.database.Dao
import kotlinx.coroutines.CoroutineScope
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

    init {
        getLocalArtists()
    }

    private fun getLocalArtists() {
        CoroutineScope(Dispatchers.IO).launch {
            //artistsLocal.postValue(localDataSource.getArtists())
            artistsLocal.postValue(dao.getAllArtists())
            Log.d("TAG", "getLocalArtists, on load, in corountine, list size = ${artistsLocal.value?.size} ")
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
        Log.d("TAG", "saveArtists in repo, size = ${list.size} ")
    }

    fun deleteAllArtists(){
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAll()
        }
    }

}