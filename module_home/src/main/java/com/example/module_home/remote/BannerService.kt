package com.example.module_home.remote

import com.example.module_home.bean.NetBanner
import retrofit2.Call
import retrofit2.http.GET

/**
Created by chene on @date 20-12-5 下午7:37
 **/
interface BannerService {

    @GET("/banner/json")
    fun getBanner(): Call<NetBanner>
}