package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.lazy.grid.*;
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.ui.theme.QuizAppTheme

class Gallery : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizAppTheme {
                    GalleryScreen()
            }
        }
    }
}

@Composable
fun GalleryScreen() {

    // Getting list of predefined images + mapping answers:
    val imageList = listOf(
        R.drawable.cat_image to "Cat",
        R.drawable.dog_image to "Dog",
        R.drawable.fish_image to "Fish",
        R.drawable.racoon_image to "Racoon",
        R.drawable.giraffe_image to "Giraffe",
        R.drawable.tiger_image to "Tiger"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFe5deef))
    ) {
        // Vertical stack of main content:
        Column() {

            // Title of page:
            Text(
                text="GALLERY",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp, bottom = 10.dp)
            )

            // Descriptive under-title:
            Text(
                text="Add/edit/delete images for quiz",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp)
            )

            // Actual grid with the images:
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(imageList) { (imageId, answer) ->
                    Column() {
                        // Image card:
                        Image(
                            painter = painterResource(id = imageId),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .aspectRatio(1f)
                                .border(5.dp, Color.White)
                        )
                        // Answer text:
                        Text(
                            text = answer,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(bottom = 5.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GalleryScreenPreview() {
    QuizAppTheme {
        GalleryScreen()
    }
}