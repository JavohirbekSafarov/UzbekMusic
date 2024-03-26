package com.javokhirbekcoder.uzbekmusic.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.javokhirbekcoder.uzbekmusic.models.ArtistsItem
import com.javokhirbekcoder.uzbekmusic.models.Music
import com.javokhirbekcoder.uzbekmusic.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
Created by Javokhirbek on 14/03/2024 at 12:18
*/

@HiltViewModel
class DownloadingFragmentViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    fun getArtists(): MutableLiveData<List<ArtistsItem>> {
        return mainRepository.getLocalArtistsList()
    }

    fun getMusicsOnline(artistId:Int):MutableLiveData<Music>{
        return mainRepository.getMusicOnline(artistId)
    }

    fun getAllMusics():MutableLiveData<Music>{
        return mainRepository.getAllMusics()
    }


}