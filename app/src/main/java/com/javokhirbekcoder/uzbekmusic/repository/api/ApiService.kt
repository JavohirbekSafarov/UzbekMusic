package com.javokhirbekcoder.uzbekmusic.repository.api

import com.javokhirbekcoder.uzbekmusic.models.Artists
import com.javokhirbekcoder.uzbekmusic.models.Music
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/*
Created by Javokhirbek on 26/02/2024 at 15:15
*/

interface ApiService {

    @GET("artist.php")
    fun getArtists(): Call<Artists>

    @GET("music.php")
    fun getMusicByArtistId(@Query("artist_id") artistId: Int): Call<Music>
    //music.php?artist_id=1

    @GET("musicAll.php")
    fun getMusics(): Call<Music>

}