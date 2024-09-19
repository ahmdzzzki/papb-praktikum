package com.example.papbpraktikum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
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
    var userInput by remember { mutableStateOf("") }
    var submittedText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (submittedText.isNotEmpty()) {
            Text(
                text = "Input: $submittedText",
                fontSize = 24.sp,
                color = Color.Blue
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        TextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("Masukkan Teks😉") },
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
                submittedText = userInput
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E88E5)
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