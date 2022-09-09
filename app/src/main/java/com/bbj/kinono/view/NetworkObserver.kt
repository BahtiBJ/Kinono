package com.bbj.kinono.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class NetworkObserver(
    context: Context,
    private val networkCallback: ConnectivityManager.NetworkCallback
) {

    private val connectivityManager =
        context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager

    private val networkRequest =
        NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

    fun registerCallBack() {
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }

    fun unregisterRequest() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

}