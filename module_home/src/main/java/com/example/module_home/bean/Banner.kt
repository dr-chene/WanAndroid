package com.example.module_home.bean

import com.example.lib_net.BaseNetBean

/**
Created by chene on @date 20-12-5 下午7:34
 **/
data class Banner(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)

class NetBanner(
    val data: List<Banner>
) : com.example.lib_net.BaseNetBean()