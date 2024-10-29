package com.example.papbpraktikum.data.model.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TugasDao {
    @Query("SELECT * FROM tugas_table")
    fun getAllTugas(): LiveData<List<Tugas>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTugas(tugas: Tugas)

    @Update
    suspend fun updateTugas(tugas: Tugas)

    @Delete
    suspend fun deleteTugas(tugas: Tugas)
}
