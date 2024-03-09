package com.javokhirbekcoder.uzbekmusic.repository.api

import com.javokhirbekcoder.uzbekmusic.models.Artists
import retrofit2.Call
import retrofit2.http.GET

/*
Created by Javokhirbek on 26/02/2024 at 15:15
*/

interface ApiService {

    @GET("artist.php")
    fun getArtists(): Call<Artists>

}