package com.example.quizapp.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.quizapp.data.PhotoManager
import com.example.quizapp.models.Photo
import com.example.quizapp.ui.theme.QuizAppTheme

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
fun QuizScreen(modifier: Modifier = Modifier) {
    val totalRounds = 3
    val photos = remember {PhotoManager.photoList.shuffled().take(totalRounds)}
    val shape = RoundedCornerShape(150.dp)

    var currentRound by remember { mutableIntStateOf(1) }
    var score by remember { mutableIntStateOf(0) }
    var currentPhoto by remember { mutableStateOf<Photo?>(null) }
    var answerOptions by remember { mutableStateOf<List<String>>(emptyList()) }

    if(photos.size < 3) return

    fun nextQuestion() {
        currentPhoto = photos[currentRound - 1]

        val wrongAnswers = PhotoManager.photoList
            .filter { it.answer != currentPhoto?.answer }
            .shuffled()
            .take(2)
            .map{ it.answer }

        answerOptions = (listOf(currentPhoto?.answer ?: "") + wrongAnswers).shuffled()
    }

    LaunchedEffect(Unit) {
        if(answerOptions.isEmpty()) nextQuestion()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFe5deef))
    ) {
        if(currentRound > totalRounds) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Score: $score / $totalRounds",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
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
            AsyncImage(
                model = currentPhoto?.uri ?: currentPhoto?.id,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(50.dp)
                    .aspectRatio(1f)
                    .clip(shape)
                    .border(5.dp, Color.Black, shape)
            )
            answerOptions.forEach { option ->
                Button(
                    onClick = {
                        if (option == currentPhoto?.answer) score++
                        currentRound++
                        if(currentRound <= totalRounds) nextQuestion()
                    },
                    shape = RoundedCornerShape(10),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp, start = 20.dp, end = 20.dp)
                ) {
                    Text(option)
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