package com.example.module_home.remote

import com.example.lib_net.bean.NetBean
import com.example.module_home.bean.Banner
import retrofit2.http.GET

/**
Created by chene on @date 20-12-5 下午7:37
 **/
interface BannerService {

    @GET("/banner/json")
    suspend fun getBanner(): NetBean<List<Banner>>
}