package com.example.quizapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quizapp.repository.AppRepo
import com.example.quizapp.room.PhotoData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/*
    Viewmodel for the gallery. Contains gallery logic.
 */
class GalleryViewModel(private val repo: AppRepo) : ViewModel() {
    private val allPhotos = repo.getPhotos().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    private val sortAscending = MutableStateFlow(true)

    val photos = combine(allPhotos, sortAscending) { list, asc ->
        if (asc) list.sortedBy { it.answer }
        else list.sortedByDescending { it.answer }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    fun addPhoto(photo: PhotoData) {
        viewModelScope.launch {
            repo.insertPhoto(photo)
        }
    }
    fun deletePhoto(photo: PhotoData) {
        viewModelScope.launch {
            repo.deletePhoto(photo)
        }
    }
    fun toggleSort() {
        sortAscending.value = !sortAscending.value
    }
    val sortAscendingState: StateFlow<Boolean>
        get() = sortAscending

    class Factory(private val repository: AppRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GalleryViewModel(repository) as T
            }
            throw IllegalArgumentException("Factory in GalleryViewModel failed.")
        }
    }
}