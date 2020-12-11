package com.example.module_coin_rank

/**
Created by chene on @date 20-12-11 下午7:28
 **/
fun Long.shouldUpdate(): Boolean {
    val curTime = System.currentTimeMillis()
    return curTime - this >= 1000 * 60 * 60 * 2
}