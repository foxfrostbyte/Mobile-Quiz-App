package com.example.quizapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
    A room entity for the photos of the quiz.
 */
@Entity(tableName = "photos")
data class PhotoData (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val answer: String,
    val uriString: String? = null,
    val drawableId: Int? = null
)