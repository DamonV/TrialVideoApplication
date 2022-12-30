package com.damonv.trialvideoapplication.data.net

import android.content.Context
import android.net.*
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.damonv.trialvideoapplication.TAG
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class ConnectivityServiceImpl @Inject constructor(
    @ApplicationContext val context: Context
): ConnectivityService {

    private val mConnectivityManager = ContextCompat.getSystemService(
            context,
            ConnectivityManager::class.java
        )

    override val networkAvailabilityFlow: Flow<Int> =
        callbackFlow {

            trySend(ConnectivityService.START)

            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                // network is available for use
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Log.d(TAG, "NetworkCallback onAvailable")
                    trySend(ConnectivityService.ON)
                }
                // lost network connection
                override fun onLost(network: Network) {
                    super.onLost(network)
                    Log.d(TAG, "NetworkCallback onLost")
                    trySend(ConnectivityService.OFF)
                }
            }

            mConnectivityManager?.registerNetworkCallback(
                NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                    .build(),
                networkCallback
            )
            Log.d(TAG, "NetworkCallback register")

            awaitClose {
                Log.d(TAG, "NetworkCallback unregister")
                mConnectivityManager?.unregisterNetworkCallback(networkCallback)
            }
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