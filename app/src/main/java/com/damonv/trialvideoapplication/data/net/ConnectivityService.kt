package com.damonv.trialvideoapplication.data.net

import kotlinx.coroutines.flow.Flow

interface ConnectivityService {

    val networkAvailabilityFlow: Flow<Int>
    fun getNetworkAvailability(): Boolean

    companion object {
        const val OFF = 0
        const val ON = 1
        const val START = 2
    }
}