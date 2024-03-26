package com.javokhirbekcoder.uzbekmusic.repository.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.javokhirbekcoder.uzbekmusic.models.Artists
import com.javokhirbekcoder.uzbekmusic.models.Music
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/*
Created by Javokhirbek on 04/03/2024 at 23:43
*/

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    private val artists = MutableLiveData<Artists>()
    private val musics = MutableLiveData<Music>()
    private val allMusics = MutableLiveData<Music>()

    fun getArtists(): MutableLiveData<Artists> {
        apiService.getArtists().enqueue(object : Callback<Artists> {
            override fun onResponse(call: Call<Artists>, response: Response<Artists>) {
                if (response.isSuccessful) {
                    if (response.body()!!.isNotEmpty()) {
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

    fun getMusics(artistId: Int):MutableLiveData<Music>{
        apiService.getMusicByArtistId(artistId).enqueue(object : Callback<Music>{
            override fun onResponse(call: Call<Music>, response: Response<Music>) {
                if (response.isSuccessful){
                    if (response.body()!!.isNotEmpty()){
                        musics.postValue(response.body())
                    }
                }
            }

            override fun onFailure(call: Call<Music>, t: Throwable) {
                Log.d("TAG", "onFailure: Internet")
            }
        })

        return musics
    }

    fun getAllMusics():MutableLiveData<Music>{
        apiService.getMusics().enqueue(object :Callback<Music>{
            override fun onResponse(call: Call<Music>, response: Response<Music>) {
                if (response.isSuccessful){
                    if (response.body()!!.isNotEmpty()){
                        allMusics.postValue(response.body())
                    }
                }
            }

            override fun onFailure(call: Call<Music>, t: Throwable) {
                Log.d("TAG", "onFailure: Internet")
            }
        })

        return allMusics
    }

}