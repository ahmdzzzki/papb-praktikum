package com.example.papbpraktikum.navigation

sealed class Screen(val route: String) {
    object Matkul : Screen("Matkul")
    object Tugas : Screen("Tugas")
    object Profil : Screen("Profil")
    object CaptureImage : Screen("CaptureImage") // Add this route for CaptureImageScreen
}
