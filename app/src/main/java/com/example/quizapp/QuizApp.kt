package com.example.quizapp

import android.app.Application
import com.example.quizapp.room.AppDatabase
import com.example.quizapp.room.PhotoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class QuizApp : Application() {
    override fun onCreate() {
        super.onCreate()

        runBlocking(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(applicationContext)
            val existing = db.dao.getPhotos().first()
            if (existing.isEmpty()) {
                val builtIn = listOf(
                    PhotoData(
                        answer = "Cat",
                        uriString = null,
                        drawableId = R.drawable.cat_image
                    ),
                    PhotoData(
                        answer = "Dog",
                        uriString = null,
                        drawableId = R.drawable.dog_image
                    ),
                    PhotoData(
                        answer = "Fish",
                        uriString = null,
                        drawableId = R.drawable.fish_image
                    ),
                    PhotoData(
                        answer = "Giraffe",
                        uriString = null,
                        drawableId = R.drawable.giraffe_image
                    ),
                    PhotoData(
                        answer = "Racoon",
                        uriString = null,
                        drawableId = R.drawable.racoon_image
                    ),
                    PhotoData(
                        answer = "Tiger",
                        uriString = null,
                        drawableId = R.drawable.tiger_image
                    )
                )
                builtIn.forEach { photo ->
                    db.dao.insertPhoto(photo)
                }
            }
        }
    }
}