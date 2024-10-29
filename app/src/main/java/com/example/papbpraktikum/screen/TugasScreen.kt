package com.example.papbpraktikum.screen

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.papbpraktikum.data.model.local.Tugas
import com.example.papbpraktikum.data.model.local.TugasRepository
import com.example.papbpraktikum.viewmodel.MainViewModel
import com.example.papbpraktikum.viewmodel.MainViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TugasScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val tugasRepository = remember { TugasRepository(context.applicationContext as Application) }

    // Logging untuk mengecek inisialisasi
    Log.d("TugasScreen", "Inisialisasi ViewModel dan Repository")

    val tugasViewModel: MainViewModel = viewModel(factory = MainViewModelFactory(tugasRepository))

    var matkulInput by remember { mutableStateOf("") }
    var detailTugasInput by remember { mutableStateOf("") }
    val tugasList by tugasViewModel.allTugas.observeAsState(emptyList())
    val isFormValid = matkulInput.isNotEmpty() && detailTugasInput.isNotEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
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

        Button(
            onClick = {
                if (isFormValid) {
                    val tugas = Tugas(matkul = matkulInput, detailTugas = detailTugasInput)
                    tugasViewModel.addTugas(tugas)
                    matkulInput = ""
                    detailTugasInput = ""
                }
            },
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Add")
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(tugasList) { tugas ->
                TugasItem(
                    tugas = tugas,
                    onToggleCompletion = { tugasViewModel.toggleCompletion(tugas) },
                    onDelete = { tugasViewModel.deleteTugas(tugas) }
                )
            }
        }
    }
}

@Composable
fun TugasItem(tugas: Tugas, onToggleCompletion: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(tugas.matkul, style = MaterialTheme.typography.bodyLarge)
                Text(tugas.detailTugas, style = MaterialTheme.typography.bodyMedium)
                Text("Completed: ${if (tugas.selesai) "Yes" else "No"}")
            }
            Row {
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
