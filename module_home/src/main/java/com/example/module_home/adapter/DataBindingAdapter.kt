package com.example.module_home.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.module_home.bean.Article
import java.text.SimpleDateFormat
import java.util.*

/**
Created by chene on @date 20-12-3 下午8:51
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

@BindingAdapter("loadOthers")
fun loadOthers(view: ViewGroup, article: Article) {
    if (article.envelopePic.isBlank()) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}

@BindingAdapter("bindEnvelopePic")
fun bindEnvelopePic(view: ImageView, url: String?) {
    if (url.isNullOrBlank()) return
    Glide.with(view.context)
        .load(url)
        .centerCrop()
        .apply(
            RequestOptions.bitmapTransform(
                RoundedCorners(4)
            ).override(view.width, view.height)
        )
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}

@BindingAdapter("bindBannerImg")
fun bindBannerImg(view: ImageView, url: String) {
    Glide.with(view.context)
        .load(url)
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}