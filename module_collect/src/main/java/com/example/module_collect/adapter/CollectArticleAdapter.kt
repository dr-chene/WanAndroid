package com.example.module_collect.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.example.module_collect.databinding.CollectRecycleItemArticleBinding
import com.example.share_article.bean.Article
import org.koin.java.KoinJavaComponent.get

/**
 *Created by chene on 20-12-19
 */
class CollectArticleAdapter(
    private val unCollect: (id: Int, originId: Int) -> Unit
) : ListAdapter<Article, RecyclerView.ViewHolder>(get(Article.ArticleDiffCallBack::class.java)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CollectArticleViewHolder(
            CollectRecycleItemArticleBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CollectArticleViewHolder).bind(getItem(position), unCollect)
    }

    class CollectArticleViewHolder(
        private val binding: CollectRecycleItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: Article, unCollect: (id: Int, originId: Int) -> Unit) {
            binding.article = article
            binding.collectArticleTitle.setOnClickListener {
                ARouter.getInstance().build("/web/activity")
                    .withString("link", article.link)
                    .navigation()
            }
            binding.collectArticleIvCollect.setOnClickListener {
                unCollect.invoke(article.id, article.originId)
            }
            binding.executePendingBindings()
        }
    }
}