package com.example.module_home.remote

import com.example.module_home.bean.NetHotKey
import retrofit2.Call
import retrofit2.http.GET

/**
Created by chene on @date 20-12-6 上午9:29
 **/
interface HotKeyService {

    @GET("/hotkey/json")
    fun getHotKey(): Call<NetHotKey>
}