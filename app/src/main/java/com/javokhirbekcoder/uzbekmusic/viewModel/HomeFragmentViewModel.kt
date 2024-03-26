package com.javokhirbekcoder.uzbekmusic.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.javokhirbekcoder.uzbekmusic.models.Artists
import com.javokhirbekcoder.uzbekmusic.models.ArtistsItem
import com.javokhirbekcoder.uzbekmusic.models.MusicEntity
import com.javokhirbekcoder.uzbekmusic.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
Created by Javokhirbek on 06/03/2024 at 13:30
*/

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    fun getOnlineArtists(): MutableLiveData<Artists> {
        return mainRepository.getOnlineArtists()
    }

    fun saveArtists(list: List<ArtistsItem>) {
        mainRepository.saveArtists(list)
    }

    fun deleteAllArtists(){
        mainRepository.deleteAllArtists()
    }

    fun saveArtist(artistsItem: ArtistsItem) {
        mainRepository.saveArtist(artistsItem)
    }

    fun getMusicsCount(): MutableLiveData<List<MusicEntity>>{
        return mainRepository.getAllMusicsOffline()
    }
}