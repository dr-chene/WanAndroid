package com.example.module_coin_rank.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lib_base.viewmodel.BaseViewModel
import com.example.lib_net.bean.NetResult
import com.example.lib_net.result
import com.example.module_coin_rank.bean.CoinRank
import com.example.module_coin_rank.repository.PageCoinRankRepository
import com.example.module_coin_rank.shouldUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CoinRankViewModel(
    private val repository: PageCoinRankRepository
) : BaseViewModel() {

    val coins: LiveData<List<CoinRank>>
        get() = _coins
    private val _coins = MutableLiveData<List<CoinRank>>()

    private var curPage = 1
    private var over = false

    fun refresh() {
        curPage = 1
        over = false
        CoroutineScope(Dispatchers.IO).launch {
            getPageCoinRank(curPage).result(
                completion = { _refreshing.postValue(false) }
            ) {
                _coins.postValue(it?.datas)
            }
        }
    }

    fun load(curList: MutableList<CoinRank>) {
        _loading.postValue(true)
        CoroutineScope(Dispatchers.IO).launch {
            getPageCoinRank(++curPage).result(
                completion = { _loading.postValue(false) }
            ) {
                it?.datas?.let { list ->
                    curList.addAll(list)
                    _coins.postValue(curList)
                }
            }
        }
    }

    private fun getPageCoinRank(page: Int) = flow {
        try {
            if (!over) {
                var localPage = repository.getLocalPageCoinRank(page)
                if (localPage == null || localPage.lastTime.shouldUpdate()) {
                    repository.getRemotePageCoinRank(page).let {
                        if (it.data == null) {
                            emit(NetResult.Failure(it.errorMsg))
                        } else {
                            it.data?.let { pageResult ->
                                localPage = pageResult
                                repository.insertPageCoinRank(pageResult)
                            }
                        }
                    }
                }
                localPage?.let {
                    curPage = it.curPage
                    over = it.over
                }
                emit(NetResult.Success(localPage))
            } else {
                emit(NetResult.Failure("没有更多数据..."))
            }
        } catch (e: Exception) {
            emit(NetResult.Failure(e.message))
        }
    }
}