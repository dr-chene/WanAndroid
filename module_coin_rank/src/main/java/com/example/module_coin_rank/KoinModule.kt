package com.example.module_coin_rank

import com.example.module_coin_rank.adapter.CoinRankRecyclerViewAdapter
import com.example.module_coin_rank.bean.CoinRank
import com.example.module_coin_rank.remote.PageCoinRankService
import com.example.module_coin_rank.repository.PageCoinRankRepository
import org.koin.dsl.module
import retrofit2.Retrofit

/**
Created by chene on @date 20-12-11 下午7:20
 **/
val coinRankModule = module {
    single { PageCoinRankRepository(get(), get()) }
    single { CoinRank.CoinRankDiffCallBack() }
    single { CoinRankRecyclerViewAdapter() }
    single { (get() as Retrofit).create(PageCoinRankService::class.java) }
}