package com.example.module_coin_rank.repository

import com.example.module_coin_rank.bean.PageCoinRank
import com.example.module_coin_rank.remote.PageCoinRankService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
Created by chene on @date 20-12-11 下午7:26
 **/
class PageCoinRankRepository(
    private val pageCoinRankDao: PageCoinRankDao,
    private val api: PageCoinRankService
) {

    fun getLocalPageCoinRank(page: Int) = pageCoinRankDao.getPageCoinRank(page)

    suspend fun insertPageCoinRank(pageCoinRank: PageCoinRank) = withContext(Dispatchers.IO) {
        pageCoinRankDao.insertPageCoinRank(pageCoinRank)
    }

    suspend fun getRemotePageCoinRank(page: Int) = api.getPageCoinRank(page)
}