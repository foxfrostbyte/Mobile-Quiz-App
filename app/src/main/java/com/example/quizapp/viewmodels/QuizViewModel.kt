package com.example.quizapp.viewmodels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.repository.AppRepo
import com.example.quizapp.room.PhotoData
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class QuizQuestion(
    val photo: PhotoData,
    val correctAnswer: String,
    val options: List<String>
)

class QuizViewModel(private val repo: AppRepo) : ViewModel() {

    var currentQuestion by mutableStateOf<QuizQuestion?>(null)
        private set
    var currentRound by mutableIntStateOf(1)
        private set
    var score by mutableIntStateOf(0)
        private set
    var totalRounds by mutableIntStateOf(3)
        private set
    var isQuizOver by mutableStateOf(false)
        private set
    var notEnoughPhotos by mutableStateOf(false)
        private set
    var isLoading by mutableStateOf(true)
        private set
    private var quizPhotoList: List<PhotoData> = emptyList()
    private var quizInitialized = false

    fun loadQuiz() {
        if (quizInitialized) return
        quizInitialized = true
        viewModelScope.launch {
            isLoading = true
            val photos = repo.getPhotos().first()
            if (photos.size < 3) {
                notEnoughPhotos = true
            } else {
                notEnoughPhotos = false
                quizPhotoList = photos.shuffled().take(minOf(3, photos.size))
                totalRounds = quizPhotoList.size
                currentRound = 1
                score = 0
                isQuizOver = false
                pickNewQuestion()
            }
            isLoading = false
        }
    }
    private fun pickNewQuestion() {
        if (currentRound > quizPhotoList.size) return
        val photo = quizPhotoList[currentRound - 1]
        val wrong = quizPhotoList
            .filter { it.answer != photo.answer }
            .shuffled()
            .take(2)
            .map { it.answer }
            currentQuestion = QuizQuestion(
            photo = photo,
            correctAnswer = photo.answer,
            options = (listOf(photo.answer) + wrong).shuffled()
        )
    }
    fun answerSelected(selected: String) {
        val q = currentQuestion ?: return
        if (selected == q.correctAnswer) score++
        currentRound++
        if (currentRound <= totalRounds) {
            pickNewQuestion()
        } else {
            isQuizOver = true
            currentQuestion = null
        }
    }
}