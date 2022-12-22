package com.damonv.trialvideoapplication.domain

import kotlinx.coroutines.flow.Flow

// domain boundaries
interface VideoRepository {
    val flowOfState: Flow<MainUiState>
}