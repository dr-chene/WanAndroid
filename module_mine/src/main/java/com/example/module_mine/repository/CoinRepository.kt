package com.example.module_mine.repository

import com.example.lib_net.request
import com.example.module_mine.remote.CoinService

class CoinRepository(
    private val coinApi: CoinService
) {
    suspend fun getRemoteCoin() = coinApi.getCoin().request()
}