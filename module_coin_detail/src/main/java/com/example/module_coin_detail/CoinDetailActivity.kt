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
import com.example.module_coin_detail.viewmodel.CoinDetailViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

@Route(path = "/coin_detail/activity")
class CoinDetailActivity : BaseActivity() {

    private lateinit var coinDetailActivityBinding: CoinDetailActivityBinding
    private val coinDetailViewModel by viewModel<CoinDetailViewModel>()
    private val coinDetailAdapter by inject<CoinDetailRecyclerViewAdapter>()

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
            load()
        }
    }

    private fun subscribe() {
        coinDetailViewModel.coins.observe(this){
            coinDetailAdapter.submitList(it)
        }
        coinDetailViewModel.refreshing.observe(this){
            coinDetailActivityBinding.coinDetailSwipe.isRefreshing = it
        }
        coinDetailViewModel.loading.observe(this){
            coinDetailActivityBinding.coinDetailLoad.root.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun refresh() = coinDetailViewModel.refresh()

    private fun load() = coinDetailViewModel.load(coinDetailAdapter.currentList.toMutableList())

}