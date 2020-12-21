package com.example.share_article.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.showToast
import com.example.lib_net.request
import com.example.lib_net.result
import com.example.share_article.bean.Article
import com.example.share_article.databinding.RecycleItemArticleBinding
import com.example.share_collect.viewmodel.ArticleUnCollectViewModel
import com.example.share_collect.viewmodel.ShareCollectViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

/**
Created by chene on @date 20-12-4 下午8:59
 **/
class ArticleRecyclerViewAdapter(
    private val isHome: Boolean,
    private val collectRepo: ShareCollectViewModel,
    private val unCollectRepo: ArticleUnCollectViewModel,
    private val isMyShare: Boolean,
    private val delete: ((Int) -> Unit)?
) : ListAdapter<Article, RecyclerView.ViewHolder>(get(Article.ArticleDiffCallBack::class.java)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ArticleViewHolder(
            RecycleItemArticleBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), collectRepo, unCollectRepo
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ArticleViewHolder).bind(getItem(position), isHome, isMyShare, delete)
    }

    class ArticleViewHolder(
        private val binding: RecycleItemArticleBinding,
        private val collectRepo: ShareCollectViewModel,
        private val unCollectRepo: ArticleUnCollectViewModel
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
                            unCollectRepo.unCollect(article.id, article.originId).request()
                                .result(null) {
                                    binding.ivArticleCollect.isSelected = false
                                    article.collect = false
                                    "文章取消收藏成功".showToast()
                                }
                        } else {
                            collectRepo.collectInnerArticle(article.id).result(null) {
                                binding.ivArticleCollect.isSelected = true
                                article.collect = true
                                "文章收藏成功".showToast()
                            }
                        }
                    }
                }
            }
        }
    }
}