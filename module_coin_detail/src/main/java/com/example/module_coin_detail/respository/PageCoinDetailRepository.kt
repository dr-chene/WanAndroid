package com.example.module_coin_detail.respository

import android.util.Log
import com.example.lib_net.bean.NetResult
import com.example.module_coin_detail.bean.PageCoinDetail
import com.example.module_coin_detail.remote.CoinDetailService
import com.example.module_coin_detail.shouldUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-11 下午11:20
 **/
class PageCoinDetailRepository(
    private val pageCoinDetailDao: PageCoinDetailDao
) {

    fun getLocalPageCoin(page: Int) = pageCoinDetailDao.getPageCoinDetail(page)

    suspend fun insertPageCoinDetail(pageCoinDetail: PageCoinDetail) = withContext(Dispatchers.IO) {
            pageCoinDetailDao.insertPageCoinDetail(pageCoinDetail)
        }
}