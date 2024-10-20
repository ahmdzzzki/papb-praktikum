package com.example.papbpraktikum.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.papbpraktikum.JadwalKuliah
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.example.papbpraktikum.Hari

@Composable
fun MatkulScreen(modifier: Modifier = Modifier) {
    val db = FirebaseFirestore.getInstance()
    var dataList by remember { mutableStateOf(listOf<JadwalKuliah>()) }

    LaunchedEffect(Unit) {
        db.collection("data_collection_papb")
            .get()
            .addOnSuccessListener { result ->
                val items = result.documents.map { document ->
                    JadwalKuliah(
                        mata_kuliah = document.getString("mata_kuliah") ?: "-",
                        hari = Hari.valueOf(document.getString("hari")?.uppercase() ?: "-"),
                        jam_mulai = document.getString("jam_mulai") ?: "-",
                        jam_selesai = document.getString("jam_selesai") ?: "-",
                        ruang = document.getString("ruang") ?: "-",
                        praktikum = document.getBoolean("praktikum") ?: false
                    )
                }
                dataList = items.sortedWith(
                    compareBy<JadwalKuliah> { it.hari.urutan }
                        .thenBy { it.jam_mulai }
                )
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Jadwal Kuliah",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.fillMaxSize()
        ) {
            items(dataList) { jadwal ->
                DataCard(data = jadwal)
            }
        }
    }
}

@Composable
fun DataCard(data: JadwalKuliah) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Mata Kuliah: ${data.mata_kuliah}", style = MaterialTheme.typography.bodyMedium)
            Text("Hari: ${data.hari}", style = MaterialTheme.typography.bodyMedium)
            Text("Jam: ${data.jam_mulai} - ${data.jam_selesai}", style = MaterialTheme.typography.bodyMedium)
            Text("Ruang: ${data.ruang}", style = MaterialTheme.typography.bodyMedium)

            if (data.praktikum) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("PRAKTIKUM", style = MaterialTheme.typography.bodyMedium, color = Color.Red)
            }
        }
    }
}