package com.example.share_collect.bean

import androidx.recyclerview.widget.DiffUtil

/**
Created by chene on @date 20-12-12 上午11:05
 **/
data class CollectWeb(
    val desc: String,
    val icon: String,
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val userId: Int,
    val visible: Int
) {
    class CollectWebDiffCallBack : DiffUtil.ItemCallback<CollectWeb>() {
        override fun areItemsTheSame(oldItem: CollectWeb, newItem: CollectWeb): Boolean {
            return oldItem.name === newItem.name
        }

        override fun areContentsTheSame(oldItem: CollectWeb, newItem: CollectWeb): Boolean {
            return oldItem.link == newItem.link
        }

    }
}