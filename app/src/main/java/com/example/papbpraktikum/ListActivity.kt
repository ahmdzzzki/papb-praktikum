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
                        nama = document.getString("nama") ?: "",
                        nim = document.getString("nim") ?: "",
                        hobi = document.getString("hobi") ?: ""
                    )
                }
                dataList = items
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
            Text("Nama: ${data.nama}", style = MaterialTheme.typography.bodyMedium)
            Text("NIM: ${data.nim}", style = MaterialTheme.typography.bodyMedium)
            Text("Hobi: ${data.hobi}", style = MaterialTheme.typography.bodyMedium)

        }
    }
}

data class DataModel(val nama: String, val nim: String, val hobi: String)