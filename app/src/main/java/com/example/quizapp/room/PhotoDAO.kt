package com.example.quizapp.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/*
    Interface to read and write to database (CRUD-stuff)
 */
@Dao
interface PhotoDAO {
    @Query("SELECT * FROM photos")
    fun getPhotos(): Flow<List<PhotoData>>

    @Upsert
    suspend fun insertPhoto(photo: PhotoData)

    @Delete
    suspend fun deletePhoto(photo: PhotoData)
}