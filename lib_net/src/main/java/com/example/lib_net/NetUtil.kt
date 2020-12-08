package com.example.lib_net

import android.accounts.NetworkErrorException
import com.example.lib_base.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
Created by chene on @date 20-12-6 下午9:18
 **/
fun <T : BaseNetBean> response(bean: T, netError: () -> Unit): T {
    when (bean.errorCode) {
        NetConstant.SUCCESS -> return bean
        else -> {
            bean.errorMsg.showToast()
            netError.invoke()
            throw NetworkErrorException("网络连接错误")
        }
    }
}

suspend fun <T> Call<T>.get() = withContext(Dispatchers.IO){
    async {
        var result: T? = null
        this@get.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                result = if (response.isSuccessful) {
                    response.body()
                } else {
                    null
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                t.message?.showToast()
                result = null
            }
        })
        return@async result
    }
}.await()

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