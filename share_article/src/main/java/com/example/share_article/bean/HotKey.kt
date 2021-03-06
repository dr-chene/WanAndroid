package com.example.share_article.bean

import androidx.recyclerview.widget.DiffUtil

/**
Created by chene on @date 20-12-6 上午10:41
 **/
data class HotKey(
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int
) {
    class HotKeyDiffCallBack : DiffUtil.ItemCallback<HotKey>() {
        override fun areItemsTheSame(oldItem: HotKey, newItem: HotKey): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HotKey, newItem: HotKey): Boolean {
            return oldItem.name == newItem.name
        }

    }
}