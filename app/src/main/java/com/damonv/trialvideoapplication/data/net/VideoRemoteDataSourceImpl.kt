package com.damonv.trialvideoapplication.data.net

import android.content.Context
import android.util.Log
import com.damonv.trialvideoapplication.R
import com.damonv.trialvideoapplication.TAG
import com.damonv.trialvideoapplication.data.di.CoroutineDispatcherIO
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import javax.inject.Inject

class VideoRemoteDataSourceImpl @Inject constructor(
    retrofit: Retrofit,
    @CoroutineDispatcherIO private val mIoDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
): VideoRemoteDataSource {

    val api = retrofit.create(WebAPI::class.java)

    interface WebAPI {
        //we do not use query params for our demo case
        @GET("backgrounds/?group=video&category_id=1")
        suspend fun queryAsync(): Response<List<WebApiDTO>>
    }

    override suspend fun getVideoList(): RemoteResultDTO =  withContext(mIoDispatcher) {
        try {
            val response = api.queryAsync()
            return@withContext if (response.isSuccessful && response.body()!= null)
                RemoteResultDTO.Success(response.body()!!)
            else{
                Log.d(TAG, "getVideoList server error ${response.code()} ${response.message()}")
                RemoteResultDTO.Error(context.resources.getString(R.string.error_server_code, response.code().toString()))
            }
        } catch (e: HttpException) {
            Log.d(TAG, "getVideoList http exception", e)
        } catch (e: Throwable) {
            Log.d(TAG, "getVideoList exception", e)
        }
        return@withContext RemoteResultDTO.Error(context.resources.getString(R.string.error_connection))
    }
}