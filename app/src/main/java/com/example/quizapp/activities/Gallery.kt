package com.example.quizapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
    val shape = RoundedCornerShape(50.dp)

    var showNameDialog by remember { mutableStateOf<Photo?>(null) }
    var photoName by remember { mutableStateOf("")}
    var sortedAsc by remember { mutableStateOf(true)};
    var sortText by remember { mutableStateOf("Z-A")};

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


    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFe5deef))
    ){
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            item(span = {GridItemSpan(2)}) {
                Text(
                    text="GALLERY",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 35.dp)
                )
            }

            item(span = {GridItemSpan(2)}) {
                Text(
                    text="Add/delete images for quiz",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp)
                )
            }

            items(photos) { photo ->
                Column {
                    AsyncImage(
                        model = photo.uri ?: photo.id,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(1f)
                            .clip(shape)
                            .border(5.dp, Color.Black, shape)
                    )
                    Text(
                        text = photo.answer,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
        Button(
            onClick = { selectImage.launch(arrayOf("image/*")) },
            shape = RoundedCornerShape(10),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, start = 20.dp, end = 20.dp)
        ) {
            Text("Add Photo")
        }
        Button(
            onClick = {
                if (sortedAsc) {
                    photos.sortByDescending { it.answer }
                    sortText = "A-Z"
                    sortedAsc = false;
                }
                else {
                    photos.sortBy { it.answer }
                    sortText = "Z-A"
                    sortedAsc = true;
                }
            },
            shape = RoundedCornerShape(10),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, start = 20.dp, end = 20.dp)
        ) {
            Text("Sort By $sortText")
        }
        Button(
            onClick = {
                PhotoManager.deleteAll()
            },
            shape = RoundedCornerShape(10),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp, start = 20.dp, end = 20.dp)
        ) {
            Text("Delete All")
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