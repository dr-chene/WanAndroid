package com.example.module_setting.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lib_net.bean.NetResult
import com.example.module_setting.remote.LoginOutService
import com.example.module_setting.repository.LoginOutRepository
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-11 上午11:24
 **/
class LoginOutViewModel(
    private val repository: LoginOutRepository
) : ViewModel(){

    fun loginOut() = repository.remoteLoginOut()
}