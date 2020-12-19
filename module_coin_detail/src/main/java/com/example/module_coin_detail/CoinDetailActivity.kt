package com.example.module_coin_detail

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.showToast
import com.example.lib_base.view.BaseActivity
import com.example.lib_net.bean.NetResult
import com.example.lib_net.bean.doFailure
import com.example.lib_net.bean.doSuccess
import com.example.lib_net.loadAction
import com.example.module_coin_detail.adapter.CoinDetailRecyclerViewAdapter
import com.example.module_coin_detail.bean.CoinDetail
import com.example.module_coin_detail.bean.PageCoinDetail
import com.example.module_coin_detail.databinding.CoinDetailActivityBinding
import com.example.module_coin_detail.respository.PageCoinDetailRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import org.koin.android.ext.android.inject

@Route(path = "/coin_detail/activity")
class CoinDetailActivity : BaseActivity() {

    private lateinit var coinDetailActivityBinding: CoinDetailActivityBinding
    private val pageCoinDetailRepository by inject<PageCoinDetailRepository>()
    private val coinDetailAdapter by inject<CoinDetailRecyclerViewAdapter>()
    private val refreshSuccess = {
        coinDetailActivityBinding.coinDetailSwipe.isRefreshing = false
    }
    private val loadSuccess = {
        coinDetailActivityBinding.coinDetailLoad.root.visibility = View.INVISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        makeStatusBarIconDark()
        coinDetailActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.coin_detail_activity)

        initView()
        initAction()
        subscribe()
    }

    private fun initView() {
        coinDetailActivityBinding.coinDetailRvContent.adapter = coinDetailAdapter
        coinDetailActivityBinding.coinDetailSwipe.isRefreshing = true
        refresh()
    }

    private fun initAction() {
        coinDetailActivityBinding.coinDetailBack.setOnClickListener {
            onBackPressed()
        }
        coinDetailActivityBinding.coinDetailSwipe.setOnRefreshListener {
            refresh()
        }
        coinDetailActivityBinding.coinDetailRvContent.loadAction {
            coinDetailActivityBinding.coinDetailLoad.root.visibility = View.VISIBLE
            load()
        }
    }

    private fun subscribe() {

    }

    private fun refresh() = dataRequest(
        pageCoinDetailRepository.refresh(),
        refreshSuccess
    ) {
        coinDetailAdapter.submitList(it)
    }

    private fun load() = dataRequest(
        pageCoinDetailRepository.load(),
        loadSuccess
    ) {
        val before = coinDetailAdapter.currentList.toMutableList()
        before.addAll(it)
        coinDetailAdapter.submitList(before)
    }

    private fun dataRequest(
        request: Flow<NetResult<PageCoinDetail?>>,
        completion: () -> Unit,
        success: (List<CoinDetail>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        request.onCompletion { completion.invoke() }
            .collectLatest {
                withContext(Dispatchers.Main) {
                    it.doSuccess { page ->
                        if (page == null) {
                            "data request error".showToast()
                        } else {
                            success.invoke(page.datas)
                            cancel()
                        }
                    }
                    it.doFailure { t ->
                        t?.showToast()
                    }
                }
            }
    }
}