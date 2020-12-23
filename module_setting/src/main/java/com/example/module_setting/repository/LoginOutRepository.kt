package com.example.module_setting.repository

import com.example.lib_net.bean.NetResult
import com.example.module_setting.remote.LoginOutService
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent

class LoginOutRepository(
    private val loginOutApi: LoginOutService
) {

    fun remoteLoginOut() = flow {
        try {
            emit(NetResult.Success(loginOutApi.loginOut()))
        } catch (e: Exception) {
            emit(NetResult.Failure(e.message))
        }
    }
}