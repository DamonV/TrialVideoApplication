package com.damonv.trialvideoapplication.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.damonv.trialvideoapplication.data.db.AppDatabase
import com.damonv.trialvideoapplication.data.db.VideoLocalDataSource
import com.damonv.trialvideoapplication.data.db.VideoLocalDataSourceImpl
import com.damonv.trialvideoapplication.data.net.ConnectivityService
import com.damonv.trialvideoapplication.data.net.ConnectivityServiceImpl
import com.damonv.trialvideoapplication.data.net.VideoRemoteDataSource
import com.damonv.trialvideoapplication.data.net.VideoRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier


@Module
@InstallIn(ViewModelComponent::class)
class DataModule() {

    @Provides
    fun providesVideoRemoteDataSource(
        dataSource: VideoRemoteDataSourceImpl
    ): VideoRemoteDataSource = dataSource

    @Provides
    fun providesRetrofit(): Retrofit =
        Retrofit.Builder()
        .baseUrl("https://dev.bgrem.deelvin.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun providesConnectivityService(
        service: ConnectivityServiceImpl
    ): ConnectivityService = service

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app-db.db"
        )
        .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
        .build()

    @Provides
    fun providesVideoLocalDataSource(
        dataSource: VideoLocalDataSourceImpl
    ): VideoLocalDataSource = dataSource

    @CoroutineDispatcherIO
    @Provides
    fun providesCoroutineDispatcherIO(): CoroutineDispatcher = Dispatchers.IO
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CoroutineDispatcherIO