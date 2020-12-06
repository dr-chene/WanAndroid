package com.example.lib_base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import org.koin.java.KoinJavaComponent.get

/**
Created by chene on @date 20-12-6 上午11:40
 **/
fun netWorkCheck(): Boolean =
    (get(Context::class.java).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).let {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            it.getNetworkCapabilities(it.activeNetwork)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
        } else {
            it.activeNetworkInfo?.isConnected ?: false
        }
    }