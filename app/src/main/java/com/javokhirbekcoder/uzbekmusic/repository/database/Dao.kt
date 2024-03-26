package com.javokhirbekcoder.uzbekmusic.repository.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.javokhirbekcoder.uzbekmusic.models.ArtistsItem
import com.javokhirbekcoder.uzbekmusic.models.MusicEntity
import com.javokhirbekcoder.uzbekmusic.repository.database.DatabaseConfig.ARTISTS_TABLE_NAME
import com.javokhirbekcoder.uzbekmusic.repository.database.DatabaseConfig.MUSICS_TABLE_NAME

/*
Created by Javokhirbek on 06/03/2024 at 13:52
*/

@Dao
interface Dao {

    // Artist
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllArtists(list: List<ArtistsItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllArtist(artistsItem: ArtistsItem)

    @Query("SELECT * FROM $ARTISTS_TABLE_NAME")
    suspend fun getAllArtists(): List<ArtistsItem>

    @Query("DELETE FROM $ARTISTS_TABLE_NAME")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteArtist(vararg artistsItem: ArtistsItem)


    // Music
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMusic(musicEntity: MusicEntity)

    @Query("SELECT * FROM $MUSICS_TABLE_NAME")
    suspend fun getAllMusics(): List<MusicEntity>

    @Query("DELETE FROM $MUSICS_TABLE_NAME")
    suspend fun deleteAllMusics()

    @Delete
    suspend fun deleteMusic(vararg musicEntity: MusicEntity)

    @Update
    suspend fun updateMusic(musicEntity: MusicEntity)


}
