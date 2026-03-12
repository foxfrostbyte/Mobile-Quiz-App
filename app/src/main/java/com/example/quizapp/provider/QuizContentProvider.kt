package com.example.quizapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.example.quizapp.room.AppDatabase
import com.example.quizapp.room.PhotoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class QuizContentProvider: ContentProvider() {
    companion object {
        const val AUTHORITY = "com.example.quizapp.provider"
        const val PHOTO_PATH = "photos"
        const val PHOTOS = 1
        const val NAME = "name"
        const val URI = "URI"
    }

    override fun query(
        uri: Uri,
        projection: Array<out String?>?,
        selection: String?,
        selectionArgs: Array<out String?>?,
        sortOrder: String?
    ): Cursor? {
        val uriMatch = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, PHOTO_PATH, PHOTOS)
        }
        if (uriMatch.match(uri) != PHOTOS) return null

        val context = context ?: return null
        val db = AppDatabase.getDatabase(context)
        val dao = db.dao
        val photos: List<PhotoData> = runBlocking(Dispatchers.IO) {
            dao.getPhotos().first()
        }

        val cursor = MatrixCursor(arrayOf(NAME, URI))
        photos.forEach { photo ->
            val uriString = when {
                !photo.uriString.isNullOrBlank() -> photo.uriString
                photo.drawableId != null ->
                    "android.resource://${context.packageName}/${photo.drawableId}"
                else -> ""
            }
            cursor.addRow(arrayOf(photo.answer, uriString))
        }
        return cursor
    }

    override fun onCreate(): Boolean {
        return true
    }

    // Unused methods, but ContentProvider as an abstract class requires it
    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(
        uri: Uri,
        values: ContentValues?
    ): Uri? {
        return null
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String?>?
    ): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String?>?
    ): Int {
        return 0
    }
}