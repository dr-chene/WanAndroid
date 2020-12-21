package com.example.module_coin_rank

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.view.BaseActivity
import com.example.lib_net.loadAction
import com.example.module_coin_rank.adapter.CoinRankRecyclerViewAdapter
import com.example.module_coin_rank.databinding.CoinRankActivityBinding
import com.example.module_coin_rank.viewmodel.CoinRankViewModel
import com.example.share_coin.Coin
import com.tencent.mmkv.MMKV
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

@Route(path = "/coin/rank/activity")
class CoinRankActivity : BaseActivity() {

    private lateinit var coinRankBinding: CoinRankActivityBinding
    private val coinRankViewModel by viewModel<CoinRankViewModel>()
    private val adapter by inject<CoinRankRecyclerViewAdapter>()
    private val myCoin by lazy {
        MMKV.defaultMMKV().decodeParcelable("coin", Coin::class.java)
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
        coinRankViewModel.coins.observe(this) {
            adapter.submitList(it)
        }
        coinRankViewModel.refreshing.observe(this) {
            coinRankBinding.coinRankContent.coinSwipe.isRefreshing = it
        }
        coinRankViewModel.loading.observe(this) {
            coinRankBinding.coinRankLoad.root.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun load() = coinRankViewModel.load(adapter.currentList.toMutableList())

    private fun refresh() = coinRankViewModel.refresh()
}