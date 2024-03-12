package com.javokhirbekcoder.uzbekmusic.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.javokhirbekcoder.uzbekmusic.models.Artists
import com.javokhirbekcoder.uzbekmusic.models.ArtistsItem
import com.javokhirbekcoder.uzbekmusic.repository.MainRepository
import com.javokhirbekcoder.uzbekmusic.repository.api.ApiService
import com.javokhirbekcoder.uzbekmusic.repository.database.Dao
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
}