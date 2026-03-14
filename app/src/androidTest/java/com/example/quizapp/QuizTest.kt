package com.example.quizapp

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.quizapp.activities.Quiz
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuizTest {
    @get:Rule
    val testRule = createAndroidComposeRule<Quiz>()

    @Test
    fun score_1of3() {
        testRule.onAllNodesWithTag("correct").onFirst().performClick()
        testRule.onAllNodesWithTag("wrong").onFirst().performClick()
        testRule.onAllNodesWithTag("wrong").onFirst().performClick()
        testRule.onNodeWithTag("score").assertTextEquals("Score: 1 / 3")
    }

    @Test
    fun score_2of3() {
        testRule.onAllNodesWithTag("correct").onFirst().performClick()
        testRule.onAllNodesWithTag("correct").onFirst().performClick()
        testRule.onAllNodesWithTag("wrong").onFirst().performClick()
        testRule.onNodeWithTag("score").assertTextEquals("Score: 2 / 3")
    }

    @Test
    fun score_3of3() {
        testRule.onAllNodesWithTag("correct").onFirst().performClick()
        testRule.onAllNodesWithTag("correct").onFirst().performClick()
        testRule.onAllNodesWithTag("correct").onFirst().performClick()
        testRule.onNodeWithTag("score").assertTextEquals("Score: 3 / 3")
    }
}

