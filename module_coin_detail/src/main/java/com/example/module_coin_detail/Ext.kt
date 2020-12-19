package com.example.module_coin_detail

/**
Created by chene on @date 20-12-11 下午11:16
 **/
fun Long.shouldUpdate(): Boolean {
    val cur = System.currentTimeMillis()
    return cur - this >= 1000 * 60 * 60 * 3
}