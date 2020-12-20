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
import com.example.lib_net.result
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

    private fun refresh() = CoroutineScope(Dispatchers.Main).launch {
        pageCoinDetailRepository.refresh().result(null, refreshSuccess) {
            coinDetailAdapter.submitList(it?.datas)
        }
    }

    private fun load() = CoroutineScope(Dispatchers.Main).launch {
        pageCoinDetailRepository.load().result(null, loadSuccess) {
            val before = coinDetailAdapter.currentList.toMutableList()
            it?.datas?.let { list -> before.addAll(list) }
            coinDetailAdapter.submitList(before)
        }
    }

}