package com.example.quizapp.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.quizapp.ui.theme.QuizAppTheme
import com.example.quizapp.viewmodels.QuizViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizapp.repository.AppRepo
import com.example.quizapp.room.AppDatabase

class Quiz : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    QuizScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    quizViewModel: QuizViewModel? = null
) {
    val shape = RoundedCornerShape(150.dp)
    val context = LocalContext.current
    val vm = quizViewModel ?: viewModel<QuizViewModel>(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val db = AppDatabase.getDatabase(context)
                val repository = AppRepo(db.dao)
                return QuizViewModel(repository) as T
            }
        }
    )

    LaunchedEffect(Unit) { vm.loadQuiz() }

    if(vm.notEnoughPhotos) {
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
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFe5deef))
    ) {
        if(vm.isQuizOver) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Score: ${vm.score} / ${vm.totalRounds}",
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
            vm.currentQuestion?.let { q ->
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
                        onClick = { vm.answerSelected(option )},
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


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    QuizAppTheme {
        QuizScreen()
    }
}