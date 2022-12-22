package com.damonv.trialvideoapplication.domain

//we don't have business logic in this layer, but it could become an entity
sealed interface MainUiState{
    data class Success(
        val videoList: List<VideoListItem>
    ): MainUiState

    data class Error(
        val msg: String?
    ): MainUiState

    object Loading: MainUiState
}