package com.example.quizapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.example.quizapp.repository.AppRepo
import com.example.quizapp.room.AppDatabase
import com.example.quizapp.room.PhotoData
import com.example.quizapp.ui.theme.QuizAppTheme
import com.example.quizapp.viewmodels.GalleryViewModel
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel

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
fun GalleryScreen(viewModel: GalleryViewModel? = null) {
    val context = LocalContext.current
    val vm = viewModel ?: viewModel<GalleryViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val db = AppDatabase.getDatabase(context)
                val repository = AppRepo(db.dao)
                return GalleryViewModel(repository) as T
            }
        }
    )
    val photos  by vm.photos.collectAsState()
    val sortAsc by vm.sortAscendingState.collectAsState(initial = true)
    val shape = RoundedCornerShape(50.dp)
    var showNameDialog by remember { mutableStateOf<Uri?>(null) }
    var photoName by remember { mutableStateOf("")}
    var selectionMode by remember { mutableStateOf(false) }
    val selectImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            if (uri.scheme == "content") {
                try {
                    context.contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) { }
            }
            showNameDialog = uri
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
            items(photos, key = { it.id }) { photo ->
                val imageUri = when {
                    !photo.uriString.isNullOrBlank() -> photo.uriString.toUri()
                    photo.drawableId != null -> "android.resource://${context.packageName}/${photo.drawableId}".toUri()
                    else -> null
                }
                Column(
                    modifier = Modifier.testTag("photoItem")
                ) {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(shape)
                            .clickable(enabled = selectionMode) {
                                vm.deletePhoto(photo)
                            }
                    ) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .border(5.dp, Color.Black, shape)
                        )
                        if (selectionMode) {
                            Box(
                                modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0x66FF0000))
                            )
                        }
                    }
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
            onClick = { vm.toggleSort() },
            shape = RoundedCornerShape(10),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, start = 20.dp, end = 20.dp)
        ) {
            Text("Sort By ${if (sortAsc) "A-Z" else "Z-A"}")
        }
        Button(
            onClick = { selectionMode = !selectionMode },
            shape = RoundedCornerShape(10),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp, start = 20.dp, end = 20.dp)
        ) {
            Text(if (selectionMode) "Return to normal mode" else "Delete")
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
                    singleLine = true,
                    modifier = Modifier.testTag("photoAnswer")
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (photoName.isNotBlank()) {
                            vm.addPhoto(
                                PhotoData(
                                    answer = photoName.trim(),
                                    uriString = photo.toString(),
                                    drawableId = null
                                )
                            )
                            showNameDialog = null
                            photoName = ""
                        }
                    }
                ) {
                    Text("Ok")
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