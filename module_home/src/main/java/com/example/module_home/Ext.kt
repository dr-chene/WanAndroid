package com.example.module_home

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.lib_base.isDebug
import org.koin.java.KoinJavaComponent

/**
Created by chene on @date 20-12-4 上午10:06
 **/
fun Int.getResColor() = ContextCompat.getColor(KoinJavaComponent.get(Context::class.java), this)

fun Long.shouldUpdate(): Boolean {
    //网络请求更新本地数据间隔时间:3小时
    val intervalTime = 1000 * 60 * 60 * if (isDebug) 0 else 3
    return (System.currentTimeMillis() - this) > intervalTime
}
