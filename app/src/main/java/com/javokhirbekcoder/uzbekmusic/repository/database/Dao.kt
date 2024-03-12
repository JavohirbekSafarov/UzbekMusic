package com.javokhirbekcoder.uzbekmusic.repository.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.javokhirbekcoder.uzbekmusic.models.ArtistsItem
import com.javokhirbekcoder.uzbekmusic.repository.database.DatabaseConfig.ARTISTS_TABLE_NAME

/*
Created by Javokhirbek on 06/03/2024 at 13:52
*/

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllArtists(list: List<ArtistsItem>)

    @Query("SELECT * FROM $ARTISTS_TABLE_NAME")
    suspend fun getAllArtists(): List<ArtistsItem>

    @Query("DELETE FROM $ARTISTS_TABLE_NAME")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteArtist(vararg artistsItem: ArtistsItem)
}
