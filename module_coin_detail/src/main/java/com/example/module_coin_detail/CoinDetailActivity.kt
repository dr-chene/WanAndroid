package com.example.module_coin_detail

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.view.BaseActivity
import com.example.lib_net.loadAction
import com.example.module_coin_detail.adapter.CoinDetailRecyclerViewAdapter
import com.example.module_coin_detail.databinding.CoinDetailActivityBinding
import com.example.module_coin_detail.viewmodel.CoinDetailViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

@Route(path = "/coin_detail/activity")
class CoinDetailActivity : BaseActivity() {

    private lateinit var binding: CoinDetailActivityBinding
    private val coinDetailViewModel by viewModel<CoinDetailViewModel>()
    private val coinDetailAdapter by inject<CoinDetailRecyclerViewAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        makeStatusBarIconDark()
        binding =
            DataBindingUtil.setContentView(this, R.layout.coin_detail_activity)

        initView()
        initAction()
        subscribe()
    }

    private fun initView() {
        binding.coinDetailRvContent.adapter = coinDetailAdapter
        binding.coinDetailSwipe.isRefreshing = true
        refresh()
    }

    private fun initAction() {
        binding.coinDetailBack.setOnClickListener {
            onBackPressed()
        }
        binding.coinDetailSwipe.setOnRefreshListener {
            refresh()
        }
        binding.coinDetailRvContent.loadAction {
            load()
        }
    }

    private fun subscribe() {
        coinDetailViewModel.coins.observe(this){
            coinDetailAdapter.submitList(it)
            binding.coinDetailNoData.root.visibility = if (it.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
        }
        coinDetailViewModel.refreshing.observe(this){
            binding.coinDetailSwipe.isRefreshing = it
        }
        coinDetailViewModel.loading.observe(this){
            binding.coinDetailLoad.root.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun refresh() = coinDetailViewModel.refresh()

    private fun load() = coinDetailViewModel.load(coinDetailAdapter.currentList.toMutableList())

}