package com.example.papbpraktikum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.papbpraktikum.ui.theme.PAPBPraktikumTheme

class ListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PAPBPraktikumTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DataListScreen()
                }
            }
        }
    }
}

@Composable
fun DataListScreen() {
    val db = FirebaseFirestore.getInstance()
    var dataList by remember { mutableStateOf(listOf<DataModel>()) }

    LaunchedEffect(Unit) {
        db.collection("data_collection_papb")
            .get()
            .addOnSuccessListener { result ->
                val items = result.documents.map { document ->
                    DataModel(
                        mata_kuliah = document.getString("mata_kuliah") ?: "-",
                        hari = Hari.valueOf(document.getString("hari")?.uppercase() ?: "-"),
                        jam_mulai = document.getString("jam_mulai") ?: "-",
                        jam_selesai = document.getString("jam_selesai") ?: "-",
                        ruang = document.getString("ruang") ?: "-",
                        praktikum = document.getBoolean("praktikum") ?: false
                    )
                }
                dataList = items.sortedWith(
                    compareBy<DataModel> { it.hari.urutan }
                        .thenBy { it.jam_mulai }
                )
            }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dataList) { data ->
            DataCard(data)
        }
    }
}

@Composable
fun DataCard(data: DataModel) {
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

data class DataModel(
    val mata_kuliah: String,
    val hari: Hari,
    val jam_mulai: String,
    val jam_selesai: String,
    val ruang: String,
    val praktikum: Boolean
)

enum class Hari(val urutan: Int) {
    SENIN(1),
    SELASA(2),
    RABU(3),
    KAMIS(4),
    JUMAT(5),
    SABTU(6),
    MINGGU(7)
}

@Preview(showBackground = true)
@Composable
fun DataCardPreview() {
    PAPBPraktikumTheme {
        DataCard(
            data = DataModel(
                mata_kuliah = "Pemrograman Dasar",
                hari = Hari.SELASA,
                jam_mulai = "10:00",
                jam_selesai = "12:02",
                ruang = "Lab G1.12",
                praktikum = true
            )
        )
    }
}