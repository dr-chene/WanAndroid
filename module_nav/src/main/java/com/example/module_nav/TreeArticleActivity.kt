package com.example.module_nav

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.showToast
import com.example.lib_net.bean.NetResult
import com.example.lib_net.bean.doFailure
import com.example.lib_net.bean.doSuccess
import com.example.lib_net.loadAction
import com.example.module_nav.databinding.ActivityTreeArticleBinding
import com.example.module_nav.repository.TreeRepository
import com.example.share_article.adapter.ArticleRecyclerViewAdapter
import com.example.share_article.bean.Article
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

@Route(path = "/tree/activity")
class TreeArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTreeArticleBinding
    private val treeRepository by inject<TreeRepository>()
    private val adapter by inject<ArticleRecyclerViewAdapter> { parametersOf(false) }

    @Autowired(name = "cid")
    lateinit var cid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tree_article)

        ARouter.getInstance().inject(this)

        initView()
        initAction()
        subscribe()

    }

    private fun initView() {
        setSupportActionBar(binding.navTreeArticleToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navTreeArticleRv.adapter = adapter
        binding.navTreeArticleSrl.isRefreshing = true
        refresh()
    }

    private fun initAction() {
        binding.navTreeArticleSrl.setOnRefreshListener {
            refresh()
        }
        binding.navTreeArticleRv.loadAction {
            load()
        }
    }

    private fun subscribe() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun refresh() {
        request(
            treeRepository.refreshTreeArticle(cid.toInt()),
            start = {},
            completion = { binding.navTreeArticleSrl.isRefreshing = false }
        ) {
            adapter.submitList(it)
        }
    }

    private fun load() {
        request(
            treeRepository.loadTreeArticle(cid.toInt()),
            start = { binding.navTreeArticleLoad.root.visibility = View.VISIBLE },
            completion = { binding.navTreeArticleLoad.root.visibility = View.INVISIBLE }
        ) {
            val cur = adapter.currentList.toMutableList()
            cur.addAll(it)
            adapter.submitList(cur)
        }
    }

    private fun request(
        request: Flow<NetResult<List<Article>?>>,
        start: () -> Unit,
        completion: () -> Unit,
        success: (List<Article>) -> Unit
    ) = CoroutineScope(Dispatchers.Main).launch {
        request.onStart { start.invoke() }
            .onCompletion { completion.invoke() }
            .collectLatest {
                withContext(Dispatchers.Main) {
                    it.doSuccess { articles ->
                        if (articles == null) {
                            "data request error".showToast()
                        } else {
                            Log.d("TAG_nav_tree", "request: ${articles}")
                            success.invoke(articles)
                            cancel()
                        }
                    }
                    it.doFailure { t ->
                        t?.showToast()
                    }
                }
            }
    }
}