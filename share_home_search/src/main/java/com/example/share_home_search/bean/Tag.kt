package com.example.share_home_search.bean

import androidx.recyclerview.widget.DiffUtil
import com.example.share_article.R
import com.example.share_home_search.getResColor
import org.koin.java.KoinJavaComponent.get
import kotlin.random.Random

/**
Created by chene on @date 20-12-3 下午5:23
 **/
data class Tag(
    val name: String,
    val url: String,
) {
    val color: Int
        get() = getRandomColor()

    companion object {
        private val colors = arrayOf(
            R.color.color_blue.getResColor(),
            R.color.color_blue_light.getResColor(),
            R.color.color_blue_lighter.getResColor(),
            R.color.color_blue_dark.getResColor(),
            R.color.color_cyan.getResColor(),
            R.color.color_red.getResColor(),
            R.color.color_red_dark.getResColor(),
            R.color.color_pink.getResColor(),
            R.color.color_purple.getResColor(),
            R.color.color_purple_dark.getResColor(),
            R.color.color_origin.getResColor(),
            R.color.color_origin_dark.getResColor(),
            R.color.color_yellow_dark.getResColor(),
            R.color.color_yellow_darker.getResColor(),
            R.color.color_green.getResColor(),
            R.color.color_green_light.getResColor(),
            R.color.color_green_dark.getResColor(),
            R.color.color_green_darker.getResColor(),
            R.color.color_brown.getResColor(),
            R.color.color_brown_light.getResColor()
        )

        fun getRandomColor() = colors[
                get(Random::class.java).nextInt(colors.size)
        ]
    }

    class TagDiffCallBack : DiffUtil.ItemCallback<Tag>() {
        override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
            return oldItem.url == newItem.url
        }

    }
}
