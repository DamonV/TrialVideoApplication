package com.damonv.trialvideoapplication.data

import android.content.Context
import android.util.Log
import com.damonv.trialvideoapplication.R
import com.damonv.trialvideoapplication.TAG
import com.damonv.trialvideoapplication.data.db.VideoEntity
import com.damonv.trialvideoapplication.data.db.VideoLocalDataSource
import com.damonv.trialvideoapplication.data.net.ConnectivityService
import com.damonv.trialvideoapplication.data.net.RemoteResultDTO
import com.damonv.trialvideoapplication.data.net.VideoRemoteDataSource
import com.damonv.trialvideoapplication.domain.MainUiState
import com.damonv.trialvideoapplication.domain.VideoListItem
import com.damonv.trialvideoapplication.domain.VideoRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(FlowPreview::class)
class VideoRepositoryImpl @Inject constructor(
    private val mVideoRemoteDataSource: VideoRemoteDataSource,
    private val mVideoLocalDataSource: VideoLocalDataSource,
    @ApplicationContext private val context: Context,
    connectivityService: ConnectivityService
): VideoRepository {

    override val flowOfState = connectivityService.networkAvailabilityFlow
        .distinctUntilChanged()
        .map { when(it){
            ConnectivityService.OFF -> false
            else -> true
        } }
        .flatMapConcat {
            Log.d(TAG, "flatMapConcat $it")
            innerMainFlow(it)
        }

    private fun innerMainFlow(networkAvailability: Boolean) = flow {

        val noInternetMsg = context.resources.getString(R.string.error_no_internet)

        //try to get videoList from DB
        val videoList = mVideoLocalDataSource.getVideoList().map {
            VideoListItem(
                fileUrl = it.fileUrl,
                posterUrl = it.posterUrl
            )
        }

        //emit main state from DB or with empty videoList
        Log.d(TAG, "emit from db ${videoList.size}")
        emit(MainUiState.Success(
            videoList = videoList,
            errMsg = if (!networkAvailability) noInternetMsg else null
        ))

        //we need to get/refresh videoList from the remote source
        if (networkAvailability) {
            val remoteResult = mVideoRemoteDataSource.getVideoList()

            if (remoteResult is RemoteResultDTO.Error) {
                Log.d(TAG, "emit from remote error")
                emit(MainUiState.Success(
                    videoList = videoList,
                    errMsg = remoteResult.msg
                ))
            } else if (remoteResult is RemoteResultDTO.Success) with(remoteResult) {

                Log.d(TAG, "emit from remote ${body.size}")
                emit(
                    MainUiState.Success(
                        videoList = body.map {
                            VideoListItem(
                                fileUrl = it.fileUrl,
                                posterUrl = it.posterUrl
                            )
                        },
                        null
                    )
                )

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
}