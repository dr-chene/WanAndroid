package com.example.module_mine

import com.example.module_mine.remote.CoinService
import com.example.module_mine.remote.ShareCollectService
import com.example.module_mine.repository.CoinRepository
import com.example.module_mine.repository.ShareCollectRepository
import org.koin.dsl.module
import retrofit2.Retrofit

/**
Created by chene on @date 20-12-10 下午12:48
 **/
val mineModule = module {
    single { (get() as Retrofit).create(CoinService::class.java) }
    single { CoinRepository() }

    single { (get() as Retrofit).create(ShareCollectService::class.java) }
    single { ShareCollectRepository() }
}