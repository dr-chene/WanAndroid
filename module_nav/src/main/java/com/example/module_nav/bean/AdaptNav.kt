package com.example.module_nav.bean

import androidx.recyclerview.widget.DiffUtil

/**
Created by chene on @date 20-12-12 下午9:39
 **/
data class AdaptNav(
    val tags: List<AdaptTag>,
    val name: String
) {
    class AdaptNavDiffCallBack : DiffUtil.ItemCallback<AdaptNav>() {
        override fun areItemsTheSame(oldItem: AdaptNav, newItem: AdaptNav): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: AdaptNav, newItem: AdaptNav): Boolean {
            return oldItem.tags == newItem.tags
        }
    }
}