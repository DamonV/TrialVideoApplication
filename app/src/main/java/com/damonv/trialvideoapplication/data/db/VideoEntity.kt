package com.damonv.trialvideoapplication.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video")
data class VideoEntity (
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val fileUrl: String?,
    val posterUrl: String?
)