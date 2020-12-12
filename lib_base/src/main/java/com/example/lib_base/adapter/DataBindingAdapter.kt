package com.example.lib_base.adapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*

/**
Created by chene on @date 20-12-11 下午11:41
 **/
@BindingAdapter("bindTime")
fun bindTime(view: TextView, time: Long) {
    if (time == 0L) {
        view.visibility = View.INVISIBLE
    }
    val cur = System.currentTimeMillis()
    val text = when ((cur - time) / 1000) {
        in 0..60 -> "刚才"
        in 60..3600 -> "${(cur - time) / 60000}分钟前"
        in 3600..86400 -> "${(cur - time) / 3600000}小时前"
        in 86400..259200 -> "${(cur - time) / 86400000}天前"
        else -> {
            val date = Date(time)
            val sdFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
            sdFormatter.format(date)
        }
    }
    view.text = text
}