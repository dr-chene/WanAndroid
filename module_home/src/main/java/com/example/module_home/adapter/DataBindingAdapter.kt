package com.example.module_home.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.module_home.bean.Article
import com.example.module_home.bean.Tag
import org.koin.java.KoinJavaComponent.get
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
    val text = when (cur - time) {
        in 0..60 -> "刚才"
        in 60..3600 -> "${(cur - time) % 60}分钟前"
        in 3600..86400 -> "${(cur - time) % 3600}小时前"
        in 86400..259200 -> "${(cur - time) % 86400}天前"
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

@BindingAdapter("bindTags")
fun bindTags(view: RecyclerView, tags: List<Tag>) {
    val tagsAdapter = get(ArticleTagsRecyclerViewAdapter::class.java)
    view.apply {
        layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        adapter = tagsAdapter
    }
    tagsAdapter.submitList(tags)
}

@BindingAdapter("bindEnvelopePic")
fun bindEnvelopePic(view: ImageView, url: String?) {
    if (url.isNullOrBlank()) view.visibility = View.GONE
    else view.visibility = View.VISIBLE

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