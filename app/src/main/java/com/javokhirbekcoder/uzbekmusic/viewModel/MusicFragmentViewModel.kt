package com.javokhirbekcoder.uzbekmusic.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.javokhirbekcoder.uzbekmusic.models.ArtistsItem
import com.javokhirbekcoder.uzbekmusic.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
Created by Javokhirbek on 11/03/2024 at 16:31
*/
@HiltViewModel
class MusicFragmentViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    fun getLocalArtists(): MutableLiveData<List<ArtistsItem>> {
        return repository.getLocalArtistsList()
    }
}