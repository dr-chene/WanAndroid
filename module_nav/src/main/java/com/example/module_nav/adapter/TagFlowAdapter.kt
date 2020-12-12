package com.example.module_nav.adapter

import android.view.LayoutInflater
import android.view.View
import com.example.module_nav.bean.AdaptTag
import com.example.module_nav.databinding.RecycleItemTagBinding
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter

/**
Created by chene on @date 20-12-12 下午4:58
 **/
class TagFlowAdapter(tags: List<AdaptTag>) : TagAdapter<AdaptTag>(tags) {

    override fun getView(parent: FlowLayout?, position: Int, t: AdaptTag?): View {
        return RecycleItemTagBinding.inflate(
            LayoutInflater.from(parent?.context), parent, false
        ).let {
            it.tag = t
            it.root
        }
    }

}