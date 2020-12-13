package com.example.share_article.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.example.share_article.bean.Article
import com.example.share_article.databinding.RecycleItemArticleBinding
import org.koin.java.KoinJavaComponent.get

/**
Created by chene on @date 20-12-4 下午8:59
 **/
class ArticleRecyclerViewAdapter(private val isHome: Boolean) :
    ListAdapter<Article, RecyclerView.ViewHolder>(get(Article.ArticleDiffCallBack::class.java)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ArticleViewHolder(
            RecycleItemArticleBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ArticleViewHolder).bind(getItem(position), isHome)
    }

    class ArticleViewHolder(
        private val binding: RecycleItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article, isHome: Boolean) {
            binding.article = article
            binding.root.setOnClickListener {
                ARouter.getInstance()
                    .build("/web/activity")
                    .withString("link", article.link)
                    .navigation()
            }
            if (!isHome) {
                binding.tvArticleDesc.visibility = View.GONE
            }
            binding.executePendingBindings()
        }
    }
}