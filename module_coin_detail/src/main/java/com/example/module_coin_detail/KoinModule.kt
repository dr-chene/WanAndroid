package com.example.module_coin_detail

import com.example.module_coin_detail.adapter.CoinDetailRecyclerViewAdapter
import com.example.module_coin_detail.bean.CoinDetail
import com.example.module_coin_detail.remote.CoinDetailService
import com.example.module_coin_detail.respository.PageCoinDetailRepository
import com.example.module_coin_detail.viewmodel.CoinDetailViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

/**
Created by chene on @date 20-12-11 下午10:49
 **/
val coinDetailModule = module {
    single { (get() as Retrofit).create(CoinDetailService::class.java) }
    single { PageCoinDetailRepository(get()) }
    single { CoinDetail.CoinDetailDiffCallBack() }

    single { CoinDetailRecyclerViewAdapter() }
    viewModel { CoinDetailViewModel(get(), get()) }
}