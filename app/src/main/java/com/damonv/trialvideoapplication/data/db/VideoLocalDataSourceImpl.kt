package com.damonv.trialvideoapplication.data.db

import javax.inject.Inject

class VideoLocalDataSourceImpl @Inject constructor(
    private val db: AppDatabase
): VideoLocalDataSource {

    override suspend fun getVideoList(): List<VideoEntity> = db.videoDao().getList()

    override suspend fun saveVideoList(list: List<VideoEntity>): List<Long> = db.videoDao().insertList(list)

}