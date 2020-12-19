package com.example.module_coin_detail.respository

import android.util.Log
import com.example.lib_net.bean.NetResult
import com.example.module_coin_detail.bean.PageCoinDetail
import com.example.module_coin_detail.remote.CoinDetailService
import com.example.module_coin_detail.shouldUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-11 下午11:20
 **/
class PageCoinDetailRepository(
    private val pageCoinDetailDao: PageCoinDetailDao
) {
    private val coinDetailApi by inject(CoinDetailService::class.java)
    private var curPage = 1
    private var over = false

    fun refresh(): Flow<NetResult<PageCoinDetail?>> {
        curPage = 1
        over = false
        return getPageCoinDetail(curPage)
    }

    fun load() = getPageCoinDetail(++curPage)

    private fun getPageCoinDetail(page: Int) = flow {
        try {
            if (!over) {
                var localPage = pageCoinDetailDao.getPageCoinDetail(page)
                if (localPage == null || localPage.lastTime.shouldUpdate()) {
                    coinDetailApi.getPageCoinDetail(page).let {
                        if (it.data != null) {
                            it.data?.let { netPage ->
                                localPage = netPage
                                over = netPage.over
                                insertPageCoinDetail(netPage)
                            }
                        } else {
                            emit(NetResult.Failure(it.errorMsg))
                        }
                    }
                }
                emit(NetResult.Success(localPage))
            } else {
                emit(NetResult.Failure("没有更多数据..."))
            }
        } catch (e: Exception) {
            Log.d("TAG_debug", "getPageCoinDetail: ${e.message}")
            emit(NetResult.Failure(e.message))
        }
    }

    private suspend fun insertPageCoinDetail(pageCoinDetail: PageCoinDetail) =
        withContext(Dispatchers.IO) {
            pageCoinDetailDao.insertPageCoinDetail(pageCoinDetail)
        }
}