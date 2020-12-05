package com.example.module_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.module_home.bean.Article
import com.example.module_home.databinding.RecycleItemArticleBinding
import org.koin.java.KoinJavaComponent.get

/**
Created by chene on @date 20-12-4 下午8:59
 **/
class ArticleRecyclerViewAdapter :
    ListAdapter<Article, RecyclerView.ViewHolder>(get(Article.ArticleDiffCallBack::class.java)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ArticleViewHolder(
            RecycleItemArticleBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ArticleViewHolder).bind(getItem(position))
    }

    class ArticleViewHolder(
        private val binding: RecycleItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            binding.article = article
            binding.root.setOnClickListener {
                TODO("方法未定义")
            }
            binding.executePendingBindings()
        }
    }
}