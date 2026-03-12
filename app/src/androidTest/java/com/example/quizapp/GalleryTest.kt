package com.example.quizapp

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.quizapp.activities.Gallery
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GalleryTest {
    @get:Rule
    val activityRule = createAndroidComposeRule<Gallery>()

    @Before
    fun initIntents() {
        Intents.init()
    }

    @After
    fun releaseIntents() {
        Intents.release()
    }

    @Test
    fun deletePhoto_Count() {
        val initialCount = activityRule.onAllNodesWithTag("photo_item").fetchSemanticsNodes().size
        if (initialCount == 0) return

        activityRule.onNodeWithText("Delete").performClick()
        activityRule.onAllNodesWithTag("photo_item").onFirst().performClick()

        val newCount = activityRule.onAllNodesWithTag("photo_item").fetchSemanticsNodes().size
        assert(newCount == initialCount - 1)
    }
}