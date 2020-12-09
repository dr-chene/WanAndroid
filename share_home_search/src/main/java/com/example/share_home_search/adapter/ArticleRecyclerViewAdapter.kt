package com.example.share_home_search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.share_article.databinding.RecycleItemArticleBinding
import com.example.share_home_search.bean.Article
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
                TODO("方法未定义")
            }
            if (!isHome) {
                binding.tvArticleDesc.visibility = View.GONE
            }
            binding.executePendingBindings()
        }
    }
}