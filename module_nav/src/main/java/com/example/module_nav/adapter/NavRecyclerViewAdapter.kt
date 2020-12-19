package com.example.module_nav.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.module_nav.bean.AdaptNav
import com.example.module_nav.bean.AdaptTag
import com.example.module_nav.databinding.RecycleItemNavBinding
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get

/**
Created by chene on @date 20-12-12 下午5:21
 **/
class NavRecyclerViewAdapter(private val click: (AdaptTag) -> Unit) :
    ListAdapter<AdaptNav, RecyclerView.ViewHolder>(get(AdaptNav.AdaptNavDiffCallBack::class.java)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NavViewHolder(
            RecycleItemNavBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NavViewHolder).bind(getItem(position), click)
    }

    class NavViewHolder(
        private val binding: RecycleItemNavBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(nav: AdaptNav, click: (AdaptTag) -> Unit) {
            binding.nav = nav
            binding.navItemTags.adapter = get(TagFlowAdapter::class.java) { parametersOf(nav.tags) }
            binding.navItemTags.setOnTagClickListener { _, position, _ ->
                click.invoke(nav.tags[position])
                true
            }
            binding.executePendingBindings()
        }
    }
}