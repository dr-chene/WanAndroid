package com.example.module_setting.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lib_net.bean.NetResult
import com.example.module_setting.remote.LoginOutService
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-11 上午11:24
 **/
class LoginOutViewModel : ViewModel(){

    private val loginOutApi by inject(LoginOutService::class.java)

    fun loginOut() = flow {
        try {
            emit(NetResult.Success(loginOutApi.loginOut()))
        } catch (e: Exception) {
            emit(NetResult.Failure(e.message))
        }
    }
}