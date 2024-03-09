package com.javokhirbekcoder.uzbekmusic.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.javokhirbekcoder.uzbekmusic.models.Artists
import com.javokhirbekcoder.uzbekmusic.repository.api.ApiService
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
    private val apiService: ApiService
):ViewModel() {

    private val list = MutableLiveData<Artists>()


    fun getArtists(): LiveData<Artists> {
        apiService.getArtists().enqueue(object : Callback<Artists> {
            override fun onResponse(call: Call<Artists>, response: Response<Artists>) {
                if (response.isSuccessful) {
                    if (!response.body()!!.isEmpty()) {
                        list.postValue(response.body())
                    }
                }
            }

            override fun onFailure(call: Call<Artists>, t: Throwable) {
                Log.d("TAG", "onFailure: Internet")
            }
        })
        return list
    }
}