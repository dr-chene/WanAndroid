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

    private lateinit var binding: CoinRankActivityBinding
    private val coinRankViewModel by viewModel<CoinRankViewModel>()
    private val adapter by inject<CoinRankRecyclerViewAdapter>()
    private val myCoin by lazy {
        MMKV.defaultMMKV().decodeParcelable("coin", Coin::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        makeStatusBarIconDark()
        binding = DataBindingUtil.setContentView(this, R.layout.coin_rank_activity)

        initView()
        initAction()
        subscribe()
    }

    private fun initView() {
        binding.coinRankContent.coinSwipe.isRefreshing = true
        binding.coinRankContent.coinRv.adapter = adapter
        binding.coinMyRank.coin = myCoin
        refresh()
    }

    private fun initAction() {
        binding.coinRankContent.coinSwipe.setOnRefreshListener {
            refresh()
        }
        binding.coinRankHead.coinIvBack.setOnClickListener {
            onBackPressed()
        }
        binding.coinRankContent.coinRv.loadAction {
            binding.coinRankLoad.root.visibility = View.VISIBLE
            load()
        }
    }

    private fun subscribe() {
        coinRankViewModel.coins.observe(this) {
            adapter.submitList(it)
            binding.coinRankNoData.root.visibility = if (it.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
        }
        coinRankViewModel.refreshing.observe(this) {
            binding.coinRankContent.coinSwipe.isRefreshing = it
        }
        coinRankViewModel.loading.observe(this) {
            binding.coinRankLoad.root.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun load() = coinRankViewModel.load(adapter.currentList.toMutableList())

    private fun refresh() = coinRankViewModel.refresh()
}