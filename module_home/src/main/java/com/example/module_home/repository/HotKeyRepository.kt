package com.example.module_home.repository

import com.example.lib_net.response
import com.example.module_home.remote.HotKeyService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent

/**
Created by chene on @date 20-12-6 上午9:31
 **/
class HotKeyRepository {

    private val hotKeyApi by KoinJavaComponent.inject(HotKeyService::class.java)

    @ExperimentalCoroutinesApi
    fun getHotKey(netError: () -> Unit) = flow {
        hotKeyApi.getHotKey()?.let {
            emit(response(it, netError))
        }
    }
}