package com.example.module_home.repository

import android.accounts.NetworkErrorException
import com.example.module_home.bean.NetHotKey
import com.example.module_home.remote.HotKeyService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.java.KoinJavaComponent.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.coroutines.resumeWithException

/**
Created by chene on @date 20-12-6 上午9:31
 **/
class HotKeyRepository {

    @ExperimentalCoroutinesApi
    fun getHotKey() = flow<NetHotKey> {
        emit(
            suspendCancellableCoroutine { coroutine ->
                get(Retrofit::class.java).create(HotKeyService::class.java).getHotKey().enqueue(
                    object : Callback<NetHotKey> {
                        override fun onResponse(
                            call: Call<NetHotKey>,
                            response: Response<NetHotKey>
                        ) {
                            if (response.isSuccessful) {
                                response.body()?.let {
                                    coroutine.resume(it) { t ->
                                        coroutine.resumeWithException(t)
                                    }
                                }
                            } else {
                                coroutine.resumeWithException(NetworkErrorException())
                            }
                        }

                        override fun onFailure(call: Call<NetHotKey>, t: Throwable) {
                            coroutine.resumeWithException(t)
                        }
                    })
            }
        )
    }
}