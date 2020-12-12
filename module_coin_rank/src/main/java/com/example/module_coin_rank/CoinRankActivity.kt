package com.example.module_coin_rank

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.showToast
import com.example.lib_base.view.BaseActivity
import com.example.lib_net.bean.NetResult
import com.example.lib_net.bean.doFailure
import com.example.lib_net.bean.doSuccess
import com.example.lib_net.loadAction
import com.example.module_coin_rank.adapter.CoinRankRecyclerViewAdapter
import com.example.module_coin_rank.bean.PageCoinRank
import com.example.module_coin_rank.databinding.CoinRankActivityBinding
import com.example.module_coin_rank.repository.PageCoinRankRepository
import com.example.share_mine_coin.Coin
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import org.koin.android.ext.android.inject

@Route(path = "/coin/rank/activity")
class CoinRankActivity : BaseActivity() {

    private lateinit var coinRankBinding: CoinRankActivityBinding
    private val pageCoinRankRepository by inject<PageCoinRankRepository>()
    private val adapter by inject<CoinRankRecyclerViewAdapter>()
    private val myCoin by lazy {
        MMKV.defaultMMKV().decodeParcelable("coin", Coin::class.java)
    }
    private val refreshSuccess = {
        coinRankBinding.coinRankContent.coinSwipe.isRefreshing = false
    }
    private val loadSuccess = {
        coinRankBinding.coinRankLoad.root.visibility = View.INVISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        makeStatusBarIconDark()
        coinRankBinding = DataBindingUtil.setContentView(this, R.layout.coin_rank_activity)

        initView()
        initAction()
        subscribe()
    }

    private fun initView() {
        coinRankBinding.coinRankContent.coinSwipe.isRefreshing = true
        coinRankBinding.coinRankContent.coinRv.adapter = adapter
        coinRankBinding.coinMyRank.coin = myCoin
        refresh()
    }

    private fun initAction() {
        coinRankBinding.coinRankContent.coinSwipe.setOnRefreshListener {
            refresh()
        }
        coinRankBinding.coinRankHead.coinIvBack.setOnClickListener {
            onBackPressed()
        }
        coinRankBinding.coinRankContent.coinRv.loadAction {
            coinRankBinding.coinRankLoad.root.visibility = View.VISIBLE
            load()
        }
    }

    private fun subscribe() {

    }

    private fun load() = dataRequest(
        pageCoinRankRepository.load(),
        loadSuccess
    ) {
        val before = adapter.currentList.toMutableList()
        before.addAll(it.datas)
        adapter.submitList(before)
    }

    private fun refresh() = dataRequest(
        pageCoinRankRepository.refresh(),
        refreshSuccess,
    ) {
        adapter.submitList(it.datas)
    }

    private fun dataRequest(
        request: Flow<NetResult<PageCoinRank?>>,
        onCompletion: () -> Unit,
        onSuccess: (page: PageCoinRank) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        request.onCompletion { onCompletion.invoke() }
            .collectLatest {
                withContext(Dispatchers.Main) {
                    it.doSuccess { page ->
                        if (page == null) {
                            "data request error".showToast()
                        } else {
                            onSuccess.invoke(page)
                            cancel()
                        }
                    }
                    it.doFailure { t ->
                        Log.d("TAG_debug", "refresh: $t")
                        t?.showToast()
                    }
                }
            }
    }
}