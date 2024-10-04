package com.example.papbpraktikum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.example.papbpraktikum.ui.theme.PAPBPraktikumTheme
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inisialisasi firebase auth
        auth = FirebaseAuth.getInstance()

        setContent {
            PAPBPraktikumTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SimpleScreen(onLoginClick = { email, password ->
                        loginWithEmail(email, password)
                    })
                }
            }
        }
    }

    // fungsi login pakai firebase authentication
    private fun loginWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("MainActivity", "Login sukses, navigasi ke ListActivity")
                    navigateToListActivity()
                } else {
                    Log.e("MainActivity", "Login gagal: ${task.exception?.message}")
                    Toast.makeText(
                        this,
                        "Login gagal: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    // fungsi untuk navigasi ke ListActivity
    private fun navigateToListActivity() {
        try {
            Log.d("MainActivity", "Navigating to ListActivity")
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.e("MainActivity", "Navigasi ke ListActivity gagal: ${e.message}")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleScreen(onLoginClick: (String, String) -> Unit) {
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var loginTriggered by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val isFormValid = emailInput.isNotEmpty() && passwordInput.isNotEmpty()
    val context = LocalContext.current
    val scope = rememberCoroutineScope() // untuk launch login logic in coroutine scope

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
                loginTriggered = true
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

    // meng-handle login logic di dalam LaunchedEffect agar composable tetap murni
    if (loginTriggered) {
        LaunchedEffect(Unit) {
            scope.launch {
                try {
                    val auth = FirebaseAuth.getInstance()
                    auth.signInWithEmailAndPassword(emailInput, passwordInput)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("SimpleScreen", "Login successful")
                                val intent = Intent(context, ListActivity::class.java)
                                context.startActivity(intent)
                            } else {
                                errorMessage = "Login failed: ${task.exception?.message}"
                                Log.e("SimpleScreen", "Login failed: ${task.exception?.message}")
                            }
                        }
                } catch (e: Exception) {
                    errorMessage = "Error during login: ${e.message}"
                }
                loginTriggered = false
            }
        }
    }
}