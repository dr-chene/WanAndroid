package com.example.module_collect

import android.view.Menu
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.showToast
import com.example.lib_net.request
import com.example.lib_net.result
import com.example.module_collect.adapter.CollectArticleAdapter
import com.example.module_collect.viewmodel.CollectArticlePageViewModel
import com.example.share_collect.viewmodel.UnCollectArticleViewModel
import com.example.share_collect.view.CollectArticleDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get

/**
 *Created by chene on 20-12-19
 */
@Route(path = "/article_collect/activity")
class CollectArticleActivity(
    private val viewModel: CollectArticlePageViewModel = get(CollectArticlePageViewModel::class.java)
): CollectActivity(viewModel) {


    private val unCollectRepository by inject<UnCollectArticleViewModel>()
    private val unCollect: (Int, Int) -> Unit = { id, originId ->
        CoroutineScope(Dispatchers.IO).launch {
            unCollectRepository.unCollect(id, originId).request().result(null) {
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

    override fun refresh() = viewModel.refresh()

    override fun load() = viewModel.load(adapter.currentList.toMutableList())

    override fun submitList() {
        viewModel.articles.observe(this){
            adapter.submitList(it)
            binding.collectNoData.root.visibility = if (it.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
        }
    }

}