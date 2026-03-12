package com.example.quizapp

import android.content.ComponentName
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.quizapp.activities.Gallery
import com.example.quizapp.activities.MainMenu
import com.example.quizapp.activities.Quiz
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainMenuTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainMenu::class.java)

    @Before
    fun initIntents() {
        Intents.init()
    }
    @After
    fun releaseIntents() {
        Intents.release()
    }

    @Test
    fun openGallery() {
        onView(withId(R.id.GalleryBtn)).perform(click())
        Intents.intended(
            IntentMatchers.hasComponent(
                ComponentName(
                    InstrumentationRegistry.getInstrumentation().targetContext,
                    Gallery::class.java
                )
            )
        )
    }

    @Test
    fun openQuiz() {
        onView(withId(R.id.QuizBtn)).perform(click())
        Intents.intended(
            IntentMatchers.hasComponent(
                ComponentName(
                    InstrumentationRegistry.getInstrumentation().targetContext,
                    Quiz::class.java
                )
            )
        )
    }
}
