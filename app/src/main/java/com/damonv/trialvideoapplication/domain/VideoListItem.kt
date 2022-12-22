package com.damonv.trialvideoapplication.domain

//we don't have business logic in this layer, but it could become an entity
data class VideoListItem(
    val fileUrl: String? = null,
    val posterUrl: String? = null
)