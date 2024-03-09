package com.javokhirbekcoder.uzbekmusic.repository.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.javokhirbekcoder.uzbekmusic.models.Artists
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/*
Created by Javokhirbek on 04/03/2024 at 23:43
*/

class RemoteDataSource @Inject constructor(private val apiService: ApiService, private val context: Context) {

    fun getArtists(): Artists {

        var list = Artists()

        apiService.getArtists().enqueue(object : Callback<Artists> {
            override fun onResponse(call: Call<Artists>, response: Response<Artists>) {
                if (response.isSuccessful) {
                    if (!response.body()!!.isEmpty()) {
                        list = response.body()!!
                        Toast.makeText(context, list.size.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<Artists>, t: Throwable) {
                Toast.makeText(context, "Internet failure", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "onFailure: Internet")
            }
        })

        Toast.makeText(context, list.size.toString(), Toast.LENGTH_SHORT).show()
        return list
    }

}