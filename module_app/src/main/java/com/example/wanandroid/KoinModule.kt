package com.example.wanandroid

import org.koin.dsl.module
import kotlin.random.Random

/**
Created by chene on @date 20-12-3 下午6:33
 **/
val appModule = module {
    single { AppDataBase.buildDatabase(get()) }
    single { get<AppDataBase>().getTopArticleDao() }
    single { get<AppDataBase>().getPageArticleDao() }
    single { get<AppDataBase>().getSearchHistoryDao() }
    single { get<AppDataBase>().getPageCoinRankDao() }
    single { get<AppDataBase>().getPageCoinDetailDao() }
    single { get<AppDataBase>().getNavDao() }
    single { get<AppDataBase>().getTreeDao() }

    single { Random(System.currentTimeMillis()) }
}