package com.damonv.trialvideoapplication.data

import android.util.Log
import com.damonv.trialvideoapplication.TAG
import com.damonv.trialvideoapplication.data.db.VideoEntity
import com.damonv.trialvideoapplication.data.db.VideoLocalDataSource
import com.damonv.trialvideoapplication.data.net.RemoteResultDTO
import com.damonv.trialvideoapplication.data.net.VideoRemoteDataSource
import com.damonv.trialvideoapplication.domain.MainUiState
import com.damonv.trialvideoapplication.domain.VideoListItem
import com.damonv.trialvideoapplication.domain.VideoRepository
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val mVideoRemoteDataSource: VideoRemoteDataSource,
    private val mVideoLocalDataSource: VideoLocalDataSource
): VideoRepository {

    override val flowOfState = flow {
        emit(MainUiState.Loading)

        val dbVideoList = mVideoLocalDataSource.getVideoList()
        if (dbVideoList.isEmpty()) {
            updateFromRemote(true)
        } else {
            Log.d(TAG, "emit from db ${dbVideoList.size}")
            emit(MainUiState.Success(
                videoList = dbVideoList.map {
                    VideoListItem(
                        fileUrl = it.fileUrl,
                        posterUrl = it.posterUrl
                    )
                }
            ))
            updateFromRemote(false)
        }

    }

    private suspend fun FlowCollector<MainUiState>.updateFromRemote(dbListIsEmpty: Boolean) {

        val remoteResult = mVideoRemoteDataSource.getVideoList()

        if (remoteResult is RemoteResultDTO.Error) {
            Log.d(TAG, "emit from remote error")
            emit(MainUiState.Error(msg = remoteResult.msg))
        } else if (remoteResult is RemoteResultDTO.Success) with(remoteResult) {
            if (dbListIsEmpty) {
                Log.d(TAG, "emit from remote ${body.size}")
                emit(MainUiState.Success(
                    videoList = body.map {
                        VideoListItem(
                            fileUrl = it.fileUrl,
                            posterUrl = it.posterUrl
                        )
                    }
                ))
            }

            Log.d(TAG, "save to db ${body.size}")
            mVideoLocalDataSource.saveVideoList(
                body.filter { it.id != null }
                .map {
                    VideoEntity(
                        id = it.id ?: "",
                        fileUrl = it.fileUrl,
                        posterUrl = it.posterUrl
                    )
                }
            )
        }
    }
}