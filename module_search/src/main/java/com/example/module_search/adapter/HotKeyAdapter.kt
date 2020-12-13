package com.example.module_search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.module_search.bean.SearchHistory
import com.example.module_search.bean.SearchHistoryTag
import com.example.module_search.databinding.RecycleItemHotKeyBinding
import com.example.share_article.bean.HotKey

/**
Created by chene on @date 20-12-8 下午9:20
 **/
class HotKeyAdapter(private val search: (SearchHistoryTag) -> Unit) :
    ListAdapter<HotKey, RecyclerView.ViewHolder>(HotKey.HotKeyDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HotKeyViewHolder(
            RecycleItemHotKeyBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HotKeyViewHolder).bind(getItem(position), search)
    }

    class HotKeyViewHolder(
        private val binding: RecycleItemHotKeyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(hotKey: HotKey, search: (SearchHistoryTag) -> Unit) {
            binding.hotKey = hotKey
            binding.root.setOnClickListener {
                search.invoke(SearchHistoryTag(hotKey.name, SearchHistory.SEARCH_TAG_KEY))
            }
            binding.executePendingBindings()
        }
    }
}