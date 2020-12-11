package com.example.module_mine.repository

import com.example.lib_net.NetResult
import com.example.module_mine.remote.CoinService
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-10 下午11:34
 **/
class CoinRepository {

    private val coinApi by inject(CoinService::class.java)

    fun getCoin() = flow {
        try {
            coinApi.getCoin().let {
                if (it.data == null) {
                    emit(NetResult.Failure(it.errorMsg))
                } else {
                    emit(NetResult.Success(it.data))
                }
            }
        } catch (e: Exception) {
            NetResult.Failure(e.message)
        }
    }
}