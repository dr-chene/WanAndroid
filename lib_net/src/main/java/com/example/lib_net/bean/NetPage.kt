package com.example.lib_net.bean

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 *Created by chene on 20-12-20
 */
data class NetPage<T>(
    val curPage: Int,
    val datas: List<T>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int,
)