package com.example.module_home.repository

import android.accounts.NetworkErrorException
import com.example.module_home.bean.NetBanner
import com.example.module_home.remote.BannerService
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
Created by chene on @date 20-12-5 下午7:39
 **/
class BannerRepository {

    @ExperimentalCoroutinesApi
    fun getBanner() = flow<NetBanner> {
        emit(
            suspendCancellableCoroutine { coroutine ->
                get(Retrofit::class.java).create(BannerService::class.java).getBanner().enqueue(
                    object : Callback<NetBanner> {
                        override fun onResponse(
                            call: Call<NetBanner>,
                            response: Response<NetBanner>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                response.body()?.let {
                                    coroutine.resume(it) { t ->
                                        coroutine.resumeWithException(t)
                                    }
                                }
                            } else {
                                coroutine.resumeWithException(NetworkErrorException())
                            }
                        }

                        override fun onFailure(call: Call<NetBanner>, t: Throwable) {
                            coroutine.resumeWithException(t)
                        }
                    })
            }
        )
    }
}