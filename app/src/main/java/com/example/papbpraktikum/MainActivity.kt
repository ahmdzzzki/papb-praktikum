package com.example.papbpraktikum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.papbpraktikum.data.model.local.TugasRepository
import com.example.papbpraktikum.navigation.Screen
import com.example.papbpraktikum.screen.MatkulScreen
import com.example.papbpraktikum.screen.ProfilScreen
import com.example.papbpraktikum.screen.TugasScreen
import com.example.papbpraktikum.ui.theme.PAPBPraktikumTheme
import com.example.papbpraktikum.viewmodel.MainViewModel
import com.example.papbpraktikum.viewmodel.TugasViewModelFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    private lateinit var tugasViewModel: MainViewModel
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

        // Initialize the repository and ViewModel
        val tugasRepository = TugasRepository(application)
        tugasViewModel = ViewModelProvider(this, TugasViewModelFactory(tugasRepository)).get(MainViewModel::class.java)

        setContent {
            PAPBPraktikumTheme {
                val navController = rememberNavController()
                MainScreen(navController = navController, tugasViewModel = tugasViewModel)
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController, tugasViewModel: MainViewModel) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Matkul.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Matkul.route) { MatkulScreen() }
            composable(Screen.Tugas.route) {
                TugasScreen(tugasViewModel = tugasViewModel)
            }
            composable(Screen.Profil.route) { ProfilScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomNavigation(
        backgroundColor = Color(0xFF1E88E5),
        contentColor = Color.White
    ) {
        BottomNavigationItem(
            label = { Text("Matkul", color = Color.White) },
            icon = { Icon(Icons.Default.List, contentDescription = "Matkul") },
            selected = navController.currentDestination?.route == Screen.Matkul.route,
            onClick = {
                navController.navigate(Screen.Matkul.route) {
                    popUpTo(Screen.Matkul.route) { inclusive = true }
                }
            },
            selectedContentColor = Color.White,
            unselectedContentColor = Color.White
        )
        BottomNavigationItem(
            label = { Text("Tugas", color = Color.White) },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = "Tugas") },
            selected = navController.currentDestination?.route == Screen.Tugas.route,
            onClick = {
                navController.navigate(Screen.Tugas.route) {
                    popUpTo(Screen.Tugas.route) { inclusive = true }
                }
            },
            selectedContentColor = Color.White,
            unselectedContentColor = Color.White
        )
        BottomNavigationItem(
            label = { Text("Profil", color = Color.White) },
            icon = { Icon(Icons.Default.Person, contentDescription = "Profil") },
            selected = navController.currentDestination?.route == Screen.Profil.route,
            onClick = {
                navController.navigate(Screen.Profil.route) {
                    popUpTo(Screen.Profil.route) { inclusive = true }
                }
            },
            selectedContentColor = Color.White,
            unselectedContentColor = Color.White
        )
    }
}
