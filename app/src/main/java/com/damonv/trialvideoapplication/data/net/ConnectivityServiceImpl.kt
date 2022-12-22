package com.damonv.trialvideoapplication.data.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class ConnectivityServiceImpl @Inject constructor(
    @ApplicationContext val context: Context
): ConnectivityService {

    private val mConnectivityManager: ConnectivityManager? by lazy {
        ContextCompat.getSystemService(
            context,
            ConnectivityManager::class.java
        ) as ConnectivityManager
    }

    override fun getNetworkAvailability(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            mConnectivityManager?.run {
                getNetworkCapabilities(activeNetwork)?.run {
                    if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
                        return true
                }
            }
        } else {
            mConnectivityManager?.activeNetworkInfo?.let {
                if (it.isConnected) return true
            }
        }
        return false
    }
}