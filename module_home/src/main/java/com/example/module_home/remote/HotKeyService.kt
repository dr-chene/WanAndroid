package com.example.module_home.remote

import com.example.lib_net.bean.NetBean
import com.example.share_article.bean.HotKey
import retrofit2.http.GET

/**
Created by chene on @date 20-12-6 上午9:29
 **/
interface HotKeyService {

    @GET("/hotkey/json")
    suspend fun getHotKey(): NetBean<List<HotKey>>
}