package com.example.quizapp.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizapp.data.PhotoManager
import com.example.quizapp.models.Photo
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
    val photos = PhotoManager.photoList
    val context = LocalContext.current
    var showNameDialog by remember { mutableStateOf<Photo?>(null) }
    var photoName by remember { mutableStateOf("")}

    val selectImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            showNameDialog = Photo(answer = "", uri = uri)
            photoName = ""
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFe5deef))
    ) {
        // Vertical stack of main content:
        Column {
            // Title of page:
            Text(
                text="GALLERY",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, bottom = 10.dp)
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
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(photos) { photo ->
                    Column {
                        // Image card:
                        if (photo.id != null) {
                            Image(
                                painter = painterResource(id = photo.id),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .aspectRatio(1f)
                                    .border(5.dp, Color.White)
                            )
                        }
                        else if (photo.uri != null){
                            val bitmap = remember(photo.uri) {
                                context.contentResolver.openInputStream(photo.uri)?.use { stream ->
                                    BitmapFactory.decodeStream(stream)?.asImageBitmap()
                                }
                            }
                            bitmap?.let {
                                Image(
                                    bitmap = it,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .aspectRatio(1f)
                                        .border(5.dp, Color.White)
                                )
                            }
                        }
                        // Answer text:
                        Text(
                            text = photo.answer,
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
            Button(
                onClick = { selectImage.launch(arrayOf("image/*")) },
                shape = RoundedCornerShape(10),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 100.dp, start = 20.dp, end = 20.dp)
            ) {
                Text("Add photo")
            }
        }
    }

    showNameDialog?.let { photo ->
        AlertDialog(
            onDismissRequest = { showNameDialog = null },
            title = { Text("Enter answer") },
            text = {
                OutlinedTextField(
                    value = photoName,
                    onValueChange = { photoName = it },
                    label = { Text("Answer") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (photoName.isNotBlank()) {
                            PhotoManager.addPhoto(Photo(answer = photoName, uri = photo.uri))
                            showNameDialog = null
                            photoName = ""
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNameDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GalleryScreenPreview() {
    QuizAppTheme {
        GalleryScreen()
    }
}