package com.example.module_collect.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.module_collect.databinding.CollectRecyleItemWebBinding
import com.example.share_collect.bean.CollectWeb
import org.koin.java.KoinJavaComponent.get

/**
 *Created by chene on 20-12-19
 */
class CollectWebAdapter(
    private val modify: (CollectWeb) -> Unit
) :
    ListAdapter<CollectWeb, RecyclerView.ViewHolder>(get(CollectWeb.CollectWebDiffCallBack::class.java)) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CollectWebViewHolder(
            CollectRecyleItemWebBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CollectWebViewHolder).bind(getItem(position), modify)
    }

    class CollectWebViewHolder(
        private val binding: CollectRecyleItemWebBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(web: CollectWeb, modify: (CollectWeb) -> Unit) {
            binding.web = web
            binding.collectWebIvEdit.setOnClickListener {
                modify.invoke(web)
            }
            binding.executePendingBindings()
        }
    }
}