package com.javokhirbekcoder.uzbekmusic.repository.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.javokhirbekcoder.uzbekmusic.models.Artists
import com.javokhirbekcoder.uzbekmusic.models.ArtistsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/*
Created by Javokhirbek on 04/03/2024 at 23:43
*/

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    private val artists = MutableLiveData<Artists>()
    fun getArtists(): MutableLiveData<Artists> {
        apiService.getArtists().enqueue(object : Callback<Artists> {
            override fun onResponse(call: Call<Artists>, response: Response<Artists>) {
                if (response.isSuccessful) {
                    if (!response.body()!!.isEmpty()) {
                        //list = response.body()!!
                        artists.postValue(response.body())
                        //Toast.makeText(context, artists.value?.size.toString(), Toast.LENGTH_SHORT).show()
                        Log.d("TAG", "online artists size = "+artists.value?.size.toString())
                    }
                }
            }

            override fun onFailure(call: Call<Artists>, t: Throwable) {
                Log.d("TAG", "onFailure: Internet")
            }
        })

        return artists
    }

}