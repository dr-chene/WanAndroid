package com.example.module_login.remote

import com.example.lib_base.bean.User
import com.example.lib_net.bean.NetBean
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

/**
Created by chene on @date 20-12-10 下午10:18
 **/
interface LoginService {

    @POST("/user/login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<NetBean<User>>

    @POST("user/register")
    suspend fun register(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("repassword") repasswrod: String
    ): Response<NetBean<User>>
}