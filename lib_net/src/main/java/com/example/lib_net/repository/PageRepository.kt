package com.example.lib_net.repository

import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetPage
import com.example.lib_net.request

/**
 *Created by chene on 20-12-20
 */
abstract class PageRepository {

    protected var curPage = 0
    protected var over = false

    fun <T> NetBean<NetPage<T>>.pageRequest() = this.request(over) { page ->
        page?.let {
            over = it.over
            curPage++
        }
    }
}