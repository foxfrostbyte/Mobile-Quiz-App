package com.example.quizapp


import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.assertCountEquals
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
    val testRule = createAndroidComposeRule<Gallery>()

    @Before
    fun initIntents() {
        Intents.init()

        val context = InstrumentationRegistry.getInstrumentation().targetContext.packageName
        val imageUri = Uri.parse("android.resource://${context}/${R.drawable.cat_image}")
        val resultData = Intent().setData(imageUri)
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)

        Intents.intending(IntentMatchers.hasAction(Intent.ACTION_OPEN_DOCUMENT)).respondWith(result)
    }

    @After
    fun releaseIntents() {
        Intents.release()
    }

    @Test
    fun addPhoto_Count() {
        val initialCount = testRule.onAllNodesWithTag("photoItem").fetchSemanticsNodes().size

        testRule.onNodeWithText("Add Photo").performClick()
        testRule.onNodeWithTag("photoAnswer").performTextInput("Cat")
        testRule.onNodeWithText("Ok").performClick()

        testRule.onAllNodesWithTag("photoItem").assertCountEquals(initialCount + 1)
    }

    @Test
    fun deletePhoto_Count() {
        val initialCount = testRule.onAllNodesWithTag("photoItem").fetchSemanticsNodes().size
        if (initialCount == 0) return

        testRule.onNodeWithText("Delete").performClick()
        testRule.onAllNodesWithTag("photoItem").onFirst().performClick()

        testRule.onAllNodesWithTag("photoItem").assertCountEquals(initialCount - 1)
    }
}