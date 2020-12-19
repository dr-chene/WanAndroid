package com.example.module_setting.remote

import com.example.lib_net.bean.NetBean
import retrofit2.http.GET

/**
Created by chene on @date 20-12-11 上午11:22
 **/
interface LoginOutService {

    @GET("/user/logout/json")
    suspend fun loginOut(): NetBean<Nothing>
}