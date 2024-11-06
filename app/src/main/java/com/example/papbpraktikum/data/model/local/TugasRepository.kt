package com.example.papbpraktikum.data.model.local

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData

class TugasRepository(application: Application) {
    private val tugasDao: TugasDao = TugasDatabase.getDatabase(application).tugasDao()

    val tugasList: LiveData<List<Tugas>> = tugasDao.getAllTugas()

    suspend fun insertTugas(tugas: Tugas) {
        tugasDao.insertTugas(tugas)
    }

    suspend fun updateTugas(tugas: Tugas) {
        tugasDao.updateTugas(tugas)
    }

    suspend fun deleteTugas(tugas: Tugas) {
        tugasDao.deleteTugas(tugas)
        Log.d("TugasRepository", "Deleted Tugas ID: ${tugas.id}, with Image URI: ${tugas.imageUri}")
    }

}
