package com.example.module_coin_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.module_coin_detail.bean.CoinDetail
import com.example.module_coin_detail.databinding.RecycleItemCoinDetailBinding
import org.koin.java.KoinJavaComponent.get

/**
Created by chene on @date 20-12-12 上午9:05
 **/
class CoinDetailRecyclerViewAdapter :
    ListAdapter<CoinDetail, RecyclerView.ViewHolder>(get(CoinDetail.CoinDetailDiffCallBack::class.java)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CoinDetailViewHolder(
            RecycleItemCoinDetailBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CoinDetailViewHolder).bind(getItem(position))
    }

    class CoinDetailViewHolder(
        private val binding: RecycleItemCoinDetailBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(coinDetail: CoinDetail) {
            binding.coinDetail = coinDetail
            binding.executePendingBindings()
        }
    }
}