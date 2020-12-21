package com.example.module_coin_detail.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lib_net.bean.NetResult
import com.example.lib_net.result
import com.example.module_coin_detail.bean.CoinDetail
import com.example.module_coin_detail.remote.CoinDetailService
import com.example.module_coin_detail.respository.PageCoinDetailRepository
import com.example.module_coin_detail.shouldUpdate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CoinDetailViewModel(
    private val repository: PageCoinDetailRepository,
    private val api: CoinDetailService
) : BaseViewModel() {

    val coins: LiveData<List<CoinDetail>>
        get() = _coins
    private val _coins = MutableLiveData<List<CoinDetail>>()

    private var curPage = 1
    private var over = false
    fun refresh() {
        curPage = 1
        over = false
        CoroutineScope(Dispatchers.IO).launch {
            getPageCoinDetail(curPage).result(
                completion = { _refreshing.postValue(false) }
            ) {
                it?.datas.let { list ->
                    _coins.postValue(list)
                }
            }
        }
    }

    fun load(curList: MutableList<CoinDetail>) {
        _loading.postValue(true)
        CoroutineScope(Dispatchers.IO).launch {
            getPageCoinDetail(++curPage).result(
                completion = { _loading.postValue(false) }
            ) {
                it?.datas?.let { list ->
                    curList.addAll(list)
                    _coins.postValue(curList)
                }
            }
        }
    }

    private fun getPageCoinDetail(page: Int) = flow {
        try {
            if (!over) {
                var localPage = repository.getLocalPageCoin(page)
                if (localPage == null || localPage.lastTime.shouldUpdate()) {
                    api.getPageCoinDetail(page).let {
                        if (it.data != null) {
                            it.data?.let { netPage ->
                                localPage = netPage
                                over = netPage.over
                                repository.insertPageCoinDetail(netPage)
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
}