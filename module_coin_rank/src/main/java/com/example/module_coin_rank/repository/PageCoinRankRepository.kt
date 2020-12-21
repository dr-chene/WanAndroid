package com.example.module_coin_rank.repository

import com.example.lib_net.bean.NetResult
import com.example.module_coin_rank.bean.PageCoinRank
import com.example.module_coin_rank.remote.PageCoinRankService
import com.example.module_coin_rank.shouldUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

/**
Created by chene on @date 20-12-11 下午7:26
 **/
class PageCoinRankRepository(
    private val pageCoinRankDao: PageCoinRankDao
) {

    private var curPage = 1
    private var over = false

    private val pageCoinRankApi by inject(PageCoinRankService::class.java)

    fun refresh(): Flow<NetResult<PageCoinRank?>> {
        curPage = 1
        over = false
        return getPageCoinRank(curPage)
    }

    fun load() = getPageCoinRank(++curPage)

    private fun getPageCoinRank(page: Int) = flow {
        try {
            if (!over) {
                var pageCoinRank = pageCoinRankDao.getPageCoinRank(page)
                if (pageCoinRank == null || pageCoinRank.lastTime.shouldUpdate()) {
                    pageCoinRankApi.getPageCoinRank(page).let {
                        if (it.data == null) {
                            emit(NetResult.Failure(it.errorMsg))
                        } else {
                            it.data?.let { pageResult ->
                                pageCoinRank = pageResult
                                insertPageCoinRank(pageResult)
                            }
                        }
                    }
                }
                pageCoinRank?.let {
                    curPage = it.curPage
                    over = it.over
                }
                emit(NetResult.Success(pageCoinRank))
            } else {
                emit(NetResult.Failure("没有更多数据..."))
            }
        } catch (e: Exception) {
            emit(NetResult.Failure(e.message))
        }
    }.flowOn(Dispatchers.IO)

    private fun insertPageCoinRank(pageCoinRank: PageCoinRank) =
        CoroutineScope(Dispatchers.IO).launch {
            pageCoinRankDao.insertPageCoinRank(pageCoinRank)
        }
}