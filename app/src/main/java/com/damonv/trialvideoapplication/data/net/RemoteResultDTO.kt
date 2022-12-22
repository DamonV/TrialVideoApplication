package com.damonv.trialvideoapplication.data.net

sealed interface RemoteResultDTO{
    data class Success(
        val body: List<WebApiDTO>
    ): RemoteResultDTO

    data class Error(
        val msg: String?
    ): RemoteResultDTO
}