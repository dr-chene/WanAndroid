package com.example.module_mine.remote

import com.example.lib_net.bean.NetBean
import com.example.share_mine_coin.Coin
import retrofit2.http.GET

/**
Created by chene on @date 20-12-10 下午11:30
 **/
interface CoinService {

    @GET("/lg/coin/userinfo/json")
    suspend fun getCoin(): NetBean<Coin>
}