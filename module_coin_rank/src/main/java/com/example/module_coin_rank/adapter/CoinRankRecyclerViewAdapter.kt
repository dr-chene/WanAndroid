package com.example.module_coin_rank.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.module_coin_rank.bean.CoinRank
import com.example.module_coin_rank.databinding.RecycleItemRankBinding
import org.koin.java.KoinJavaComponent.get

/**
Created by chene on @date 20-12-11 下午7:49
 **/
class CoinRankRecyclerViewAdapter :
    ListAdapter<CoinRank, RecyclerView.ViewHolder>(get(CoinRank.CoinRankDiffCallBack::class.java)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CoinRankViewHolder(
            RecycleItemRankBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CoinRankViewHolder).bind(getItem(position))
    }

    class CoinRankViewHolder(
        private val binding: RecycleItemRankBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(coinRank: CoinRank) {
            binding.coinRank = coinRank
            binding.executePendingBindings()
        }
    }
}