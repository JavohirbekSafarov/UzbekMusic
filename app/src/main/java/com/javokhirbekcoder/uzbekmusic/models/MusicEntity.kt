package com.javokhirbekcoder.uzbekmusic.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
Created by Javokhirbek on 13/03/2024 at 16:25
*/
@Entity
data class MusicEntity(
    @PrimaryKey()
    val id: Int,
    val artist: String,
    val duration: String,
    val music_img: String,
    val music_name: String,
    val music_name_path: String,
    var is_have_offline:Boolean = false
)
