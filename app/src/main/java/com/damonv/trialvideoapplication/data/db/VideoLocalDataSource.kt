package com.damonv.trialvideoapplication.data.db

interface VideoLocalDataSource {

    suspend fun getVideoList(): List<VideoEntity>

    suspend fun saveVideoList(list: List<VideoEntity>): List<Long>
}