package com.example.quizapp.models

import android.net.Uri

data class Photo(
    val id: Int? = null,
    val answer: String,
    val uri: Uri? = null,
)