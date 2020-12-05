package com.example.module_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.module_home.bean.Banner
import com.example.module_home.databinding.RecycleItemBannerBinding
import com.youth.banner.adapter.BannerAdapter

/**
Created by chene on @date 20-12-5 下午8:14
 **/
class MyBannerAdapter(data: List<Banner>) : BannerAdapter<Banner, RecyclerView.ViewHolder>(data) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BannerViewHolder(
            RecycleItemBannerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindView(
        holder: RecyclerView.ViewHolder?,
        data: Banner?,
        position: Int,
        size: Int
    ) {
        data?.let {
            (holder as BannerViewHolder).bind(it)
        }
    }

    class BannerViewHolder(
        private val binding: RecycleItemBannerBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(banner: Banner) {
            binding.banner = banner
            binding.root.setOnClickListener {
                TODO("未定义")
            }
            binding.executePendingBindings()
        }
    }
}