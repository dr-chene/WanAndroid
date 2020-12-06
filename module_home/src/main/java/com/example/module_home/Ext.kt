package com.example.module_home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.lib_base.HotKey
import com.example.lib_base.isDebug
import com.google.gson.Gson
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

private const val animDuration = 400L

fun TextView.change(fromY: Float, toY: Float, finalY: Float) {
    val move = ObjectAnimator.ofFloat(this, "y", fromY, toY)
    val toAlpha = if (finalY <= toY) 1f else 0f
    val show = ObjectAnimator.ofFloat(this, "alpha", toAlpha)
    AnimatorSet().apply {
        interpolator = AccelerateDecelerateInterpolator()
        duration = animDuration
        play(move).with(show)
        start()
    }
}