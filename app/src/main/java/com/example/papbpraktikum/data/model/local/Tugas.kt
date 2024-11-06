package com.example.papbpraktikum.data.model.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "tugas_table")
@Parcelize
data class Tugas(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "matkul")
    var matkul: String,

    @ColumnInfo(name = "detail_tugas")
    var detailTugas: String,

    @ColumnInfo(name = "image_uri")
    var imageUri: String? = null,

    @ColumnInfo(name = "selesai")
    var selesai: Boolean = false
) : Parcelable