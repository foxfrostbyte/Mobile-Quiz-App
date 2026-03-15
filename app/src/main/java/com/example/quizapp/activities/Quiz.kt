package com.example.quizapp.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.quizapp.ui.theme.QuizAppTheme
import com.example.quizapp.viewmodels.QuizViewModel
import com.example.quizapp.repository.AppRepo
import com.example.quizapp.room.AppDatabase

class Quiz : ComponentActivity() {
    private val viewModel: QuizViewModel by viewModels {
        QuizViewModel.Factory(
            AppRepo(AppDatabase.getDatabase(applicationContext).dao)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                    QuizScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun QuizScreen(viewModel: QuizViewModel) {
    val shape = RoundedCornerShape(150.dp)
    val context = LocalContext.current
    LaunchedEffect(Unit) { viewModel.loadQuiz() }

    if(viewModel.notEnoughPhotos) {
        val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
        Dialog(onDismissRequest = { backDispatcher?.onBackPressed() }) {
            Column(
                modifier = Modifier
                    .background(Color(0xFFaca2ce), RoundedCornerShape(16.dp))
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "Not enough photos",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "You need at least 3 photos in order to play the quiz.",
                    color = Color.Black)
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { backDispatcher?.onBackPressed() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Return to main menu")
                }
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFe5deef))
    ) {
        if(viewModel.isQuizOver) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Score: ${viewModel.score} / ${viewModel.totalRounds}",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("score")
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        else {
            Text(
                text = "QUIZ",
                color = Color.Black,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, bottom = 10.dp)
            )
            Text(
                text = "Choose the correct answer!",
                color = Color.Black,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            viewModel.currentQuestion?.let { q ->
                val imageUri = when {
                    !q.photo.uriString.isNullOrBlank() -> q.photo.uriString.toUri()
                    q.photo.drawableId != null -> "android.resource://${context.packageName}/${q.photo.drawableId}".toUri()
                    else -> null
                }
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(50.dp)
                        .aspectRatio(1f)
                        .clip(shape)
                        .border(5.dp, Color.Black, shape)
                )
                q.options.forEach { option ->
                    Button(
                        onClick = { viewModel.answerSelected(option )},
                        shape = RoundedCornerShape(10),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp, start = 20.dp, end = 20.dp)
                            .testTag(if (option == q.correctAnswer) "correct" else "wrong")
                    )
                    { Text(option)}
                }
            }
        }
    }
}