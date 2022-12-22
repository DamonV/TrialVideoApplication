package com.damonv.trialvideoapplication.data.net

interface VideoRemoteDataSource {

    suspend fun getVideoList(): RemoteResultDTO

}