package com.example.module_coin_rank.remote

import com.example.lib_net.bean.NetBean
import com.example.module_coin_rank.bean.PageCoinRank
import retrofit2.http.GET
import retrofit2.http.Path

/**
Created by chene on @date 20-12-11 下午7:24
 **/
interface PageCoinRankService {

    @GET("/coin/rank/{page}/json")
    suspend fun getPageCoinRank(@Path("page") page: Int): NetBean<PageCoinRank>
}