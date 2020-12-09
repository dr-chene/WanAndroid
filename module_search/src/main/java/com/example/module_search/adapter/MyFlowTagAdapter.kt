package com.example.module_search.adapter

import android.view.LayoutInflater
import android.view.View
import com.example.module_search.bean.FlowTag
import com.example.module_search.databinding.RecycleItemFlowTagBinding
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter

/**
Created by chene on @date 20-12-8 下午9:52
 **/
class MyFlowTagAdapter(datas: List<FlowTag>) : TagAdapter<FlowTag>(datas) {

    override fun getView(parent: FlowLayout?, position: Int, t: FlowTag?): View {
        val binding = RecycleItemFlowTagBinding.inflate(
            LayoutInflater.from(parent?.context), parent, false
        )
        binding.flowTag = getItem(position)
        return binding.root
    }

}