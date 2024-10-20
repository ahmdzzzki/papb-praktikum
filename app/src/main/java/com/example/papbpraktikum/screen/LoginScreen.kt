package com.example.papbpraktikum.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginClick: (String, String) -> Unit) {
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var loginTriggered by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isFormValid = emailInput.isNotEmpty() && passwordInput.isNotEmpty()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text("Email") },
            leadingIcon = {
                Icon(Icons.Filled.AccountCircle, contentDescription = "Icon Email")
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
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text("Password") },
            leadingIcon = {
                Icon(Icons.Filled.Lock, contentDescription = "Icon Password")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            visualTransformation = PasswordVisualTransformation(), // hide password input
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                if (isFormValid) {
                    loginTriggered = true
                } else {
                    errorMessage = "Email atau password tidak boleh kosong"
                }
            },
            enabled = isFormValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormValid) Color(0xFF1E88E5) else Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text("Login", color = Color.White)
        }

        errorMessage?.let {
            Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }
    }

    if (loginTriggered) {
        LaunchedEffect(Unit) {
            scope.launch {
                onLoginClick(emailInput, passwordInput)
                loginTriggered = false
            }
        }
    }
}