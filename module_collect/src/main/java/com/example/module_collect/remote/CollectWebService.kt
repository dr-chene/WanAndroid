package com.example.module_collect.remote

import com.example.lib_net.bean.NetBean
import com.example.share_collect.bean.CollectWeb
import retrofit2.http.GET

/**
 *Created by chene on 20-12-19
 */
interface CollectWebService {

    @GET("/lg/collect/usertools/json")
    suspend fun getWebs(): NetBean<List<CollectWeb>>
}