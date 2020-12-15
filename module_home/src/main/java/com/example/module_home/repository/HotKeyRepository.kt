package com.example.module_home.repository

import com.example.lib_net.bean.NetResult
import com.example.module_home.remote.HotKeyService
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent

/**
Created by chene on @date 20-12-6 上午9:31
 **/
class HotKeyRepository {

    private val hotKeyApi by KoinJavaComponent.inject(HotKeyService::class.java)

    fun getHotKey() = flow {
        emit(
            try {
                NetResult.Success(hotKeyApi.getHotKey().data)
            } catch (e: Exception) {
                NetResult.Failure(e.message)
            }
        )
    }
}