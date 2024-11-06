package com.example.papbpraktikum.screen

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import coil.compose.rememberImagePainter
import com.example.papbpraktikum.data.model.local.Tugas
import com.example.papbpraktikum.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TugasScreen(
    modifier: Modifier = Modifier,
    tugasViewModel: MainViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var imageCapture: ImageCapture? = remember { ImageCapture.Builder().build() }
    var previewImageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    var isImageDialogOpen by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var matkulInput by remember { mutableStateOf("") }
    var detailTugasInput by remember { mutableStateOf("") }
    var imageFileName by remember { mutableStateOf("No image captured") }

    val tugasList by tugasViewModel.allTugas.observeAsState(emptyList())
    val isFormValid = matkulInput.isNotEmpty() && detailTugasInput.isNotEmpty()
    val previewView = remember { PreviewView(context) }

    val permissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[android.Manifest.permission.CAMERA] == true &&
            permissions[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] == true) {
            startCamera(context, lifecycleOwner, imageCapture, cameraExecutor, previewView)
        } else {
            Toast.makeText(context, "Required permissions denied", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        item {
            // Input fields for task details
            TextField(
                value = matkulInput,
                onValueChange = { matkulInput = it },
                label = { Text("Nama Matkul") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            TextField(
                value = detailTugasInput,
                onValueChange = { detailTugasInput = it },
                label = { Text("Detail Tugas") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            TextField(
                value = imageFileName,
                onValueChange = {},
                label = { Text("Nama File Gambar") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // Camera preview
            AndroidView(
                factory = { previewView },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(8.dp)
            )
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                // Start Camera Button
                Button(
                    onClick = {
                        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            startCamera(context, lifecycleOwner, imageCapture, cameraExecutor, previewView)
                        } else {
                            permissionsLauncher.launch(
                                arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            )
                        }
                    }
                ) {
                    Text("Start Camera")
                }

                // Add Task Button
                Button(
                    onClick = {
                        if (isFormValid) {
                            val tugas = Tugas(
                                matkul = matkulInput,
                                detailTugas = detailTugasInput,
                                imageUri = previewImageUri?.toString() // Store image URI in Tugas
                            )
                            tugasViewModel.addTugas(tugas)
                            matkulInput = ""
                            detailTugasInput = ""
                            previewImageUri = null // Reset preview after adding
                            imageFileName = "No image captured"
                        }
                    },
                    enabled = isFormValid
                ) {
                    Text("Add")
                }
            }
        }

        item {
            // Capture Image Button
            Button(
                onClick = {
                    captureImage(context, imageCapture) { uri ->
                        previewImageUri = uri // Update preview URI
                        imageFileName = uri.lastPathSegment ?: "No image name"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Capture Image")
            }
        }

        item {
            // Preview captured image
            previewImageUri?.let { uri ->
                Image(
                    painter = rememberImagePainter(uri),
                    contentDescription = "Preview Gambar",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Task list with preview images
        items(tugasList) { tugas ->
            TugasItem(
                tugas = tugas,
                onToggleCompletion = { tugasViewModel.toggleCompletion(tugas) },
                onDelete = { tugasViewModel.deleteTugas(tugas) },
                onImageClick = {uri ->
                    selectedImageUri = uri
                    isImageDialogOpen = true
                }
            )
        }
    }
    if (isImageDialogOpen && selectedImageUri != null) {
        Dialog(onDismissRequest = { isImageDialogOpen = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .clickable { isImageDialogOpen = false } // Close dialog on background click
            ) {
                Image(
                    painter = rememberImagePainter(selectedImageUri),
                    contentDescription = "Full Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .align(Alignment.Center),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}


@Composable
fun TugasItem(
    tugas: Tugas,
    onToggleCompletion: () -> Unit,
    onDelete: () -> Unit,
    onImageClick: (Uri) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Column for text and image
            Column(modifier = Modifier.weight(1f)) {
                Text(tugas.matkul, style = MaterialTheme.typography.bodyLarge)
                Text(tugas.detailTugas, style = MaterialTheme.typography.bodyMedium)
                Text("Completed: ${if (tugas.selesai) "Yes" else "No"}")

                // Display image preview if available
                tugas.imageUri?.let { uri ->
                    Image(
                        painter = rememberImagePainter(uri),
                        contentDescription = "Task Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onImageClick(Uri.parse(uri)) },
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Row for action buttons, given fixed width to prevent being pushed out
            Row(
                modifier = Modifier.width(60.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onToggleCompletion) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Toggle Completion")
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Task")
                }
            }
        }
    }
}



fun startCamera(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    imageCapture: ImageCapture?,
    cameraExecutor: ExecutorService,
    previewView: PreviewView
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        // Set up Preview use case to display the camera preview
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider) // Set the SurfaceProvider to show the preview
        }

        // Select the back camera as the default
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            // Unbind all use cases before rebinding
            cameraProvider.unbindAll()

            // Bind the camera use cases to the lifecycle
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        } catch (exc: Exception) {
            Log.e("TugasScreen", "Use case binding failed", exc)
        }
    }, ContextCompat.getMainExecutor(context))
}


fun captureImage(
    context: Context,
    imageCapture: ImageCapture?,
    onImageCaptured: (Uri) -> Unit
) {
    val name = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "IMG_$name.jpg")
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Images")
        }
    }

    val outputOptions = ImageCapture.OutputFileOptions.Builder(
        context.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    ).build()

    imageCapture?.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                output.savedUri?.let { uri ->
                    onImageCaptured(uri)
                    Toast.makeText(context, "Image saved: $uri", Toast.LENGTH_SHORT).show()
                } ?: run {
                    Toast.makeText(context, "Image capture failed: URI is null", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("TugasScreen", "Photo capture failed: ${exception.message}", exception)
                Toast.makeText(context, "Photo capture failed", Toast.LENGTH_SHORT).show()
            }
        }
    )
}