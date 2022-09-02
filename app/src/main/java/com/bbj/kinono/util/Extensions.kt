package com.bbj.kinono.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper

fun Context.isOnline() : Boolean{
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
    }
    return false
}

fun postDelay(delay : Long,run : () -> Unit){
    Handler(Looper.getMainLooper()).postDelayed({
        run()
    },delay)
}

fun dip2px(context: Context, dpValue: Float): Float {
    val scale: Float = context.getResources().getDisplayMetrics().density
    return (dpValue * scale + 0.5f)
}