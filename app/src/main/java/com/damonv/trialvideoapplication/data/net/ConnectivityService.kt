package com.damonv.trialvideoapplication.data.net

interface ConnectivityService {

    fun getNetworkAvailability(): Boolean
}