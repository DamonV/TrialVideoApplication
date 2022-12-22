package com.damonv.trialvideoapplication.data.net

import com.google.gson.annotations.SerializedName

data class WebApiDTO(
    val id: String?,
    @SerializedName("file_url") val fileUrl: String?,
    @SerializedName("small_poster_url") val posterUrl: String?
)