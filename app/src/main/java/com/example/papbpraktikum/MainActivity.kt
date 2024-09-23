package com.example.papbpraktikum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.papbpraktikum.ui.theme.PAPBPraktikumTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PAPBPraktikumTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SimpleScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleScreen() {
    var namaInput by remember { mutableStateOf("") }
    var nimInput by remember { mutableStateOf("") }
    var submittedTextNama by remember { mutableStateOf("") }
    var submittedTextNim by remember { mutableStateOf("") }

    // UI state
    val isFormValid = namaInput.isNotEmpty() && nimInput.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (submittedTextNama.isNotEmpty() && submittedTextNim.isNotEmpty()) {
            Text(
                text = "Nama: $submittedTextNama",
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = "NIM: $submittedTextNim",
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        TextField(
            value = namaInput,
            onValueChange = { namaInput = it },
            label = { Text("Masukkan Nama") },
            leadingIcon = {
                Icon(Icons.Filled.AccountCircle, contentDescription = "Icon Profil")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFBBDEFB),
                focusedIndicatorColor = Color(0xFF1E88E5),
                unfocusedIndicatorColor = Color(0xFF1E88E5),
                focusedLabelColor = Color(0xFF1E88E5)
            ),
            textStyle = TextStyle(fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = nimInput,
            onValueChange = { nimInput = it },
            label = { Text("Masukkan NIM") },
            leadingIcon = {
                Icon(Icons.Filled.Star, contentDescription = "Icon NIM")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFBBDEFB),
                focusedIndicatorColor = Color(0xFF1E88E5),
                unfocusedIndicatorColor = Color(0xFF1E88E5),
                focusedLabelColor = Color(0xFF1E88E5)
            ),
            textStyle = TextStyle(fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                submittedTextNama = namaInput
                submittedTextNim = nimInput
            },
            enabled = isFormValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormValid) Color(0xFF1E88E5) else Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(text = "Submit", color = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleScreenPreview() {
    PAPBPraktikumTheme {
        SimpleScreen()
    }
}