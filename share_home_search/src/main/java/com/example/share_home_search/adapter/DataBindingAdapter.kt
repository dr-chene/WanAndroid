package com.example.share_home_search.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.share_home_search.bean.Article
import com.example.share_home_search.toSearchTitleColorString

/**
Created by chene on @date 20-12-8 下午8:53
 **/

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
