package com.example.quizapp.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PhotoData::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract val dao: PhotoDAO
}