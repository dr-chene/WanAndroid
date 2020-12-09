package com.example.share_home_search.adapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.share_home_search.bean.Article
import com.example.share_home_search.toSearchTitleColorString
import java.text.SimpleDateFormat
import java.util.*

/**
Created by chene on @date 20-12-8 下午8:53
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

@BindingAdapter("bindAuthor")
fun bindAuthor(view: TextView, article: Article) {
    val text = when {
        article.author.isNotEmpty() -> {
            "作者：${article.author}"
        }
        article.shareUser.isNotEmpty() -> {
            "分享人：${article.shareUser}"
        }
        else -> {
            ""
        }
    }
    view.text = text
}

@BindingAdapter("bindText")
fun bindText(view: TextView, text: String){
    view.text = text.toSearchTitleColorString()
}
