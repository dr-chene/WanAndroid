package com.example.lib_net

/**
 * Created by chene on @date 20-12-4 下午2:36
 */
data class NetBean<T>(
    val data: T?,
    val errorCode: Int,
    val errorMsg: String
)