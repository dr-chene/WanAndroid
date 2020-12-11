package com.example.module_coin_rank.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
Created by chene on @date 20-12-11 下午2:40
 **/
@BindingAdapter("bindLevel")
fun bindLevel(view: TextView, level: Int) {
    val text = "level:$level"
    view.text = text
}