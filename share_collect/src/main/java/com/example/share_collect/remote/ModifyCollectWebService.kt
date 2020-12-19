package com.example.share_collect.remote

import com.example.lib_net.bean.NetBean
import com.example.share_collect.bean.CollectWeb
import retrofit2.http.POST
import retrofit2.http.Query

/**
 *Created by chene on 20-12-19
 */
interface ModifyCollectWebService {

    @POST("/lg/collect/deletetool/json")
    suspend fun delete(@Query("id") id: Int): NetBean<Any>

    @POST("/lg/collect/updatetool/json\n")
    suspend fun modify(
        @Query("id") id: Int,
        @Query("name") name: String,
        @Query("link") link: String
    ): NetBean<CollectWeb>
}