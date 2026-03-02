package com.example.quizapp.data

import androidx.compose.runtime.mutableStateListOf
import com.example.quizapp.R
import com.example.quizapp.models.Photo

object PhotoManager {
    var photoList: MutableList<Photo> = mutableStateListOf()

    init {
        photoList.add(Photo(R.drawable.cat_image, "Cat"))
        photoList.add(Photo(R.drawable.dog_image, "Dog"))
        photoList.add(Photo(R.drawable.fish_image, "Fish"))
        photoList.add(Photo(R.drawable.racoon_image, "Racoon"))
        photoList.add(Photo(R.drawable.giraffe_image, "Giraffe"))
        photoList.add(Photo(R.drawable.tiger_image, "Tiger"))

        photoList.sortBy { it.answer }
    }

    fun addPhoto(photo: Photo) {
        photoList.add(photo)
    }
}