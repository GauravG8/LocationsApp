package com.mylocations.managers

import android.content.Context
import android.net.ConnectivityManager

/**
 * Manager class to provide information about network
 */
class NetworkManager(val context: Context) {

    // Return whether the device is connected to the internet or not
    val isConnected: Boolean?
        get(){
            val connectionManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectionManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
}