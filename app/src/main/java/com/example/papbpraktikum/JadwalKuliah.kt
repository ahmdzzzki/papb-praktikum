package com.example.papbpraktikum

data class JadwalKuliah(
    val mata_kuliah: String,
    val hari: Hari,
    val jam_mulai: String,
    val jam_selesai: String,
    val ruang: String,
    val praktikum: Boolean
)