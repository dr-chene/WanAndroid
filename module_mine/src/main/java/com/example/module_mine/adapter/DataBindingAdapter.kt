package com.example.module_mine.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.share_mine_coin.Coin

/**
Created by chene on @date 20-12-10 下午5:36
 **/
@BindingAdapter("bindEmail")
fun bindEmail(view: TextView, email: String?) {
    val text = if (email.isNullOrEmpty()) "暂无"
    else email
    view.text = text
}

@BindingAdapter("bindNickname")
fun bindNickname(view: TextView, nickname: String?) {
    val text = if (nickname.isNullOrEmpty()) "请登录后查看"
    else nickname
    view.text = text
}

@BindingAdapter("bindInt")
fun bindInt(view: TextView, d: Int) {
    val text = if (d <= 0) "--"
    else d.toString()
    view.text = text
}

@BindingAdapter("bindCoin")
fun bindCoin(view: TextView, coin: com.example.share_mine_coin.Coin?) {
    val text = if (coin == null) {
        ""
    } else {
        "当前积分： ${coin.coinCount} "
    }
    view.text = text
}