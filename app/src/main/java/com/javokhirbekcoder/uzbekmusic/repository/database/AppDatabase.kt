package com.javokhirbekcoder.uzbekmusic.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.javokhirbekcoder.uzbekmusic.models.ArtistsItem
import com.javokhirbekcoder.uzbekmusic.models.MusicEntity

/*
Created by Javokhirbek on 06/03/2024 at 13:52
*/

@Database(entities = [ArtistsItem::class, MusicEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun Dao(): Dao
}