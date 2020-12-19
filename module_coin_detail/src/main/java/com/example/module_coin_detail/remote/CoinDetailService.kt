package com.example.module_coin_detail.remote

import com.example.lib_net.bean.NetBean
import com.example.module_coin_detail.bean.PageCoinDetail
import retrofit2.http.GET
import retrofit2.http.Path

/**
Created by chene on @date 20-12-11 下午11:18
 **/
interface CoinDetailService {

    @GET("/lg/coin/list/{page}/json")
    suspend fun getPageCoinDetail(@Path("page") page: Int): NetBean<PageCoinDetail>
}