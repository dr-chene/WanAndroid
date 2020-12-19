package com.example.share_collect.remote

import com.example.lib_net.bean.NetBean
import com.example.share_collect.bean.CollectArticle
import com.example.share_collect.bean.CollectWeb
import retrofit2.http.POST
import retrofit2.http.Query

/**
Created by chene on @date 20-12-12 上午10:52
 **/
interface ShareCollectService {

    @POST("/lg/user_article/add/json")
    suspend fun shareArticle(
        @Query("title") title: String,
        @Query("link") link: String
    ): NetBean<String?>

    @POST("/lg/collect/add/json")
    suspend fun collectArticle(
        @Query("title") title: String,
        @Query("author") author: String,
        @Query("link") link: String
    ): NetBean<CollectArticle>

    @POST("/lg/collect/addtool/json")
    suspend fun collectWeb(
        @Query("name") name: String,
        @Query("link") link: String
    ): NetBean<CollectWeb>
}