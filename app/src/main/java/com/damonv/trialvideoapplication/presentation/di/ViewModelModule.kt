package com.damonv.trialvideoapplication.presentation.di

import com.damonv.trialvideoapplication.data.VideoRepositoryImpl
import com.damonv.trialvideoapplication.domain.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelModule {

    @Binds
    fun providesVideoRepository(videoRepositoryImpl: VideoRepositoryImpl): VideoRepository
}