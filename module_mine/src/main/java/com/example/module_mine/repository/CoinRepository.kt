package com.example.module_mine.repository

import com.example.lib_net.bean.NetResult
import com.example.lib_net.request
import com.example.lib_net.result
import com.example.module_mine.remote.CoinService
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-10 下午11:34
 **/
class CoinRepository(
    private val coinApi: CoinService
) {

    suspend fun getCoin() = coinApi.getCoin().request()

}