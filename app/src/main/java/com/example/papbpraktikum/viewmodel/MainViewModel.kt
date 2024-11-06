package com.example.papbpraktikum.viewmodel

import androidx.lifecycle.*
import com.example.papbpraktikum.data.model.local.Tugas
import com.example.papbpraktikum.data.model.local.TugasRepository
import kotlinx.coroutines.launch

class MainViewModel(private val tugasRepository: TugasRepository) : ViewModel() {

    val allTugas: LiveData<List<Tugas>> = tugasRepository.tugasList

    fun addTugas(tugas: Tugas) {
        viewModelScope.launch {
            tugasRepository.insertTugas(tugas)
        }
    }

    fun deleteTugas(tugas: Tugas) {
        viewModelScope.launch {
            tugasRepository.deleteTugas(tugas)
        }
    }

    fun toggleCompletion(tugas: Tugas) {
        viewModelScope.launch {
            val updatedTugas = tugas.copy(selesai = !tugas.selesai)
            tugasRepository.updateTugas(updatedTugas)
        }
    }
}
