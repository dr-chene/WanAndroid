package com.example.module_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.module_home.bean.Tag
import com.example.module_home.databinding.RecycleItemTagBinding
import org.koin.java.KoinJavaComponent.get

/**
Created by chene on @date 20-12-4 上午10:28
 **/
class ArticleTagsRecyclerViewAdapter :
    ListAdapter<Tag, RecyclerView.ViewHolder>(get(Tag.TagDiffCallBack::class.java)) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TagViewHolder(
            RecycleItemTagBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TagViewHolder).bind(getItem(position))
    }

    class TagViewHolder(
        private val binding: RecycleItemTagBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tag: Tag) {
            binding.tag = tag
            binding.executePendingBindings()
        }
    }
}