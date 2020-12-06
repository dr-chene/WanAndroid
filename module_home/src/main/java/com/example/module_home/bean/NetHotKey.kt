package com.example.module_home.bean

import com.example.lib_net.BaseNetBean
import com.example.lib_base.HotKey

/**
Created by chene on @date 20-12-6 上午9:26
 **/

data class NetHotKey(
    val data: List<HotKey>
) : com.example.lib_net.BaseNetBean()