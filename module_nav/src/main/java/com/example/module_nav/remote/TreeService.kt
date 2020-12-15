package com.example.module_nav.remote

import com.example.lib_net.bean.NetBean
import com.example.module_nav.bean.Tree
import retrofit2.http.GET

/**
Created by chene on @date 20-12-12 下午8:33
 **/
interface TreeService {

    @GET("/tree/json")
    suspend fun getData(): NetBean<List<Tree>>
}