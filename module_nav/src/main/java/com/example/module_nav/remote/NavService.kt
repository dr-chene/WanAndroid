package com.example.module_nav.remote

import com.example.lib_net.bean.NetBean
import com.example.module_nav.bean.Nav
import retrofit2.http.GET

/**
Created by chene on @date 20-12-12 下午4:25
 **/
interface NavService {

    @GET("/navi/json")
    suspend fun getNavs(): NetBean<List<Nav>>
}