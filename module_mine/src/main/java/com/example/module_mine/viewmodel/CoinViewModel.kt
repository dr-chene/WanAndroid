package com.example.module_mine.viewmodel

import com.example.lib_net.request
import com.example.module_mine.remote.CoinService
import com.example.module_mine.repository.CoinRepository

/**
Created by chene on @date 20-12-10 下午11:34
 **/
class CoinViewModel(
    private val repository: CoinRepository
) {
    suspend fun getCoin() = repository.getRemoteCoin()
}