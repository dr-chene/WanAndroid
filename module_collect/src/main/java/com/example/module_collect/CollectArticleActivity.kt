package com.example.module_collect

import android.view.Menu
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.showToast
import com.example.lib_net.request
import com.example.lib_net.result
import com.example.module_collect.adapter.CollectArticleAdapter
import com.example.module_collect.repository.CollectArticlePageRepository
import com.example.share_collect.repository.ArticleUnCollectRepository
import com.example.share_collect.view.CollectArticleDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 *Created by chene on 20-12-19
 */
@Route(path = "/article_collect/activity")
class CollectArticleActivity : CollectActivity() {

    private val repository by inject<CollectArticlePageRepository>()
    private val unCollectRepository by inject<ArticleUnCollectRepository>()
    private val unCollect: (Int, Int) -> Unit = { id, originId ->
        CoroutineScope(Dispatchers.IO).launch {
            unCollectRepository.unCollect(id, originId).request().result(null, null) {
                "文章取消收藏成功".showToast()
                binding.collectSrl.isRefreshing = true
                refresh()
            }
        }
    }
    private val adapter by inject<CollectArticleAdapter> { parametersOf(unCollect) }

    override fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.collect_menu_article, menu)
        return true
    }

    override fun add() {
        CollectArticleDialog(this).apply {
            setOnDismissListener {
                binding.collectSrl.isRefreshing = true
                refresh()
            }
            show()
        }
    }

    override fun refresh() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.refresh().result(
                start = null,
                completion = {
                    binding.collectSrl.isRefreshing = false
                }
            ) {
                adapter.submitList(it)
            }
        }
    }

    override fun load() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.load().result(
                start = { CoroutineScope(Dispatchers.Main).launch { binding.collectLoad.root.visibility = View.VISIBLE } },
                completion = { CoroutineScope(Dispatchers.Main).launch { binding.collectLoad.root.visibility = View.INVISIBLE } }
            ) {
                val before = adapter.currentList.toMutableList()
                before.addAll(it)
                adapter.submitList(before)
            }
        }
    }
}