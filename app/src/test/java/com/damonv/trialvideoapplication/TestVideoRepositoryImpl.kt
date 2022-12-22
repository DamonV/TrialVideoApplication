package com.damonv.trialvideoapplication

import com.damonv.trialvideoapplication.data.net.VideoRemoteDataSource
import com.damonv.trialvideoapplication.domain.MainUiState
import com.damonv.trialvideoapplication.domain.VideoListItem
import com.damonv.trialvideoapplication.domain.VideoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TestVideoRepositoryImpl @Inject constructor(
    val videoRemoteDataSource: VideoRemoteDataSource
): VideoRepository {

    override val flowOfState = flow {
        emit(MainUiState.Loading)
        delay(1000)

        emit(MainUiState.Success(
            mutableListOf(
                VideoListItem("asset:///preview-e6rqan9a.mp4", R.drawable.poster),
                VideoListItem("asset:///preview-a5muvscr.mp4", R.drawable.poster_1),
                VideoListItem("asset:///preview-q8q6_2ri.mp4", R.drawable.poster_2),
                VideoListItem("asset:///preview-e6rqan9a.mp4", R.drawable.poster_3),
                VideoListItem("asset:///preview-a5muvscr.mp4", R.drawable.poster_2),
                VideoListItem("asset:///preview-e6rqan9a.mp4", R.drawable.poster),
                VideoListItem("asset:///preview-q8q6_2ri.mp4", R.drawable.poster_1),
                VideoListItem("asset:///preview-e6rqan9a.mp4", R.drawable.poster_3),
                VideoListItem("asset:///preview-q8q6_2ri.mp4", R.drawable.poster_2),
                VideoListItem("asset:///preview-a5muvscr.mp4", R.drawable.poster),
                VideoListItem("asset:///preview-q8q6_2ri.mp4", R.drawable.poster_3),
                VideoListItem("asset:///preview-e6rqan9a.mp4", R.drawable.poster_1)
            )
        ))
    }
}