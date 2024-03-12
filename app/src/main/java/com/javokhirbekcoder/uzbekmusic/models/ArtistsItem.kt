package com.javokhirbekcoder.uzbekmusic.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ArtistsItem(
    val artist: String,
    @PrimaryKey
    val id: Int,
    val img: String
)