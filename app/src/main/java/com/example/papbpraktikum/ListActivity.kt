package com.example.papbpraktikum

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import com.example.papbpraktikum.ui.theme.PAPBPraktikumTheme

class ListActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PAPBPraktikumTheme {
                Scaffold(
                    topBar = {
                        ListActivityTopBar(
                            onGithubProfileClick = {
                                val intent = Intent(this@ListActivity, GithubProfileActivity::class.java)
                                startActivity(intent)
                            }
                        )
                    },
                    content = { paddingValues ->
                        DataListScreen(modifier = Modifier.padding(paddingValues))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListActivityTopBar(onGithubProfileClick: () -> Unit) {
    TopAppBar(
        title = { Text("Jadwal Kuliah") },
        actions = {
            IconButton(onClick = { onGithubProfileClick() }) {
                Image(
                    painter = rememberImagePainter("https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png"),
                    contentDescription = "GitHub Logo",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    )
}

@Composable
fun DataListScreen(modifier: Modifier = Modifier) {
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

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dataList) { data ->
            DataCard(data)
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

@Preview(showBackground = true)
@Composable
fun DataCardPreview() {
    PAPBPraktikumTheme {
        DataCard(
            data = JadwalKuliah(
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
