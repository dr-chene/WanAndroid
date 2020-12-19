package com.example.module_home.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.share_article.bean.Article

/**
Created by chene on @date 20-12-3 下午8:51
 **/

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