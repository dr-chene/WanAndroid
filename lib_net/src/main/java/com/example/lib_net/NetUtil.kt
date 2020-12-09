package com.example.lib_net

import android.accounts.NetworkErrorException
import android.util.Log
import com.example.lib_base.showToast
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resumeWithException

/**
Created by chene on @date 20-12-6 下午9:18
 **/

@ExperimentalCoroutinesApi
inline fun <reified T> Call<T>.get() = flow<T?> {
    emit(
        suspendCancellableCoroutine { cor ->
            this@get.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    Log.d("TAG_remote", "onResponse: ${response.body().toString()}")
                    if (cor.isActive) {
                        if (response.isSuccessful) {
                            cor.resume(response.body()) { t ->
                                cor.resumeWithException(t)
                            }
                        } else {
                            cor.resumeWithException(NetworkErrorException())
                        }
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    t.message?.showToast()
                    cor.resumeWithException(t)
                }
            })
        }
    )
}

//fun netWorkCheck(): Boolean =
//    (KoinJavaComponent.get(Context::class.java)
//        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).let {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            it.getNetworkCapabilities(it.activeNetwork)
//                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
//        } else {
//            it.activeNetworkInfo?.isConnected ?: false
//        }
//    }
//
//
//class NetBroadcastReceiver() : BroadcastReceiver() {
//
//    val netWorkState: LiveData<Int>
//        get() = _netWorkState
//    private val _netWorkState = MutableLiveData<Int>()
//
//    override fun onReceive(context: Context?, intent: Intent?) {
//        intent ?: return
//        if (intent.action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//            _netWorkState.postValue(getNetWorkState(context))
//        }
//    }
//
//}
//
//private const val NETWORK_NONE = -1
//private const val NETWORK_MOBILE = 0
//private const val NETWORK_WIFI = 1
//private const val NETWORK_AVAILABLE = 2
//private fun getNetWorkState(context: Context?): Int {
//    context ?: return NETWORK_NONE
//    val connect = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    return connect.activeNetworkInfo?.let {
//        if (it.isConnected) {
//            when (it.type) {
//                ConnectivityManager.TYPE_WIFI -> {
//                    NETWORK_WIFI
//                }
//                ConnectivityManager.TYPE_MOBILE -> {
//                    NETWORK_MOBILE
//                }
//                else -> NETWORK_AVAILABLE
//            }
//        } else {
//            NETWORK_NONE
//        }
//    } ?: NETWORK_NONE
//}