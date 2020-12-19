package com.example.share_collect.remote

import com.example.lib_net.bean.NetBean
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

/**
 *Created by chene on 20-12-19
 */
interface UnCollectArticleService {

    @POST("/lg/uncollect_originId/{id}/json")
    suspend fun unCollect(@Path("id") id: Int): NetBean<Any>

    @FormUrlEncoded
    @POST("/lg/uncollect/{id}/json")
    suspend fun unCollectOnMe(@Path("id") id: Int, @Field("originId") originId: Int): NetBean<Any>
}