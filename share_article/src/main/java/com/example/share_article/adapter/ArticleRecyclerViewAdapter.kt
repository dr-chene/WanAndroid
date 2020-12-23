package com.example.share_article.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.showToast
import com.example.lib_net.request
import com.example.lib_net.result
import com.example.share_article.bean.Article
import com.example.share_article.databinding.RecycleItemArticleBinding
import com.example.share_collect.viewmodel.ShareCollectViewModel
import com.example.share_collect.viewmodel.UnCollectArticleViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.get

/**
Created by chene on @date 20-12-4 下午8:59
 **/
class ArticleRecyclerViewAdapter(
    private val isHome: Boolean,
    private val collectViewModel: ShareCollectViewModel,
    private val unCollectArticleViewModel: UnCollectArticleViewModel,
    private val isMyShare: Boolean,
    private val delete: ((Int) -> Unit)?
) : ListAdapter<Article, RecyclerView.ViewHolder>(get(Article.ArticleDiffCallBack::class.java)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ArticleViewHolder(
            RecycleItemArticleBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), collectViewModel, unCollectArticleViewModel
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ArticleViewHolder).bind(getItem(position), isHome, isMyShare, delete)
    }

    class ArticleViewHolder(
        private val binding: RecycleItemArticleBinding,
        private val collectViewModel: ShareCollectViewModel,
        private val unCollectArticleViewModel: UnCollectArticleViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article, isHome: Boolean, isMyShare: Boolean, delete: ((Int) -> Unit)?) {
            binding.article = article
            binding.tvArticleTitle.setOnClickListener {
                ARouter.getInstance()
                    .build("/web/activity")
                    .withString("link", article.link)
                    .withString("cate", "article")
                    .withString("collect", article.collect.toString())
                    .withString("id", article.id.toString())
                    .withString("originId", article.originId.toString())
                    .navigation()
            }
            if (!isHome) {
                binding.tvArticleDesc.visibility = View.GONE
            }
            if (isMyShare) {
                binding.ivArticleDelete.visibility = View.VISIBLE
                binding.ivArticleDelete.setOnClickListener {
                    delete?.invoke(article.id)
                }
            }
            binding.tvArticleAuthor.setOnClickListener {
                if (article.userId != -1) {
                    ARouter.getInstance()
                        .build("/cid/activity")
                        .withString("cid", article.userId.toString())
                        .withString("cate", "share")
                        .navigation()
                }
            }
            if (article.collect) {
                binding.ivArticleCollect.isSelected = true
            }
            collect(article)
            binding.executePendingBindings()
        }

        private fun collect(article: Article) {
            binding.ivArticleCollect.setOnClickListener {
                synchronized(article.collect) {
                    CoroutineScope(Dispatchers.IO).launch {
                        if (article.collect) {
                            withContext(Dispatchers.Main){
                                AlertDialog.Builder(binding.root.context).apply {
                                    setMessage("是否取消收藏?")
                                    setPositiveButton("删除") { _, _ ->
                                        unCollect(article)
                                    }
                                    setNegativeButton("取消操作") { _, _ ->
                                        "操作取消".showToast()
                                    }
                                }.show().apply {
                                    getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
                                }
                            }
                        } else {
                            collectViewModel.collectInnerArticle(article.id).result(null) {
                                binding.ivArticleCollect.isSelected = true
                                article.collect = true
                                "文章收藏成功".showToast()
                            }
                        }
                    }
                }
            }
        }

        private fun unCollect(article: Article) = CoroutineScope(Dispatchers.IO).launch {
            unCollectArticleViewModel.unCollect(article.id, article.originId).request()
                .result(null) {
                    binding.ivArticleCollect.isSelected = false
                    article.collect = false
                    "文章取消收藏成功".showToast()
                }
        }
    }
}