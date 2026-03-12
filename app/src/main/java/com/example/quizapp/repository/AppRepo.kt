package com.example.quizapp.repository

import com.example.quizapp.room.PhotoDAO
import com.example.quizapp.room.PhotoData
import kotlinx.coroutines.flow.Flow

/*
    Repository that wraps the PhotoDAO, following UDF architecture.
 */
class AppRepo(private val dao: PhotoDAO) {
    fun getPhotos(): Flow<List<PhotoData>> = dao.getPhotos();
    suspend fun insertPhoto(photo: PhotoData) = dao.insertPhoto(photo);
    suspend fun deletePhoto(photo: PhotoData) = dao.deletePhoto(photo);
}