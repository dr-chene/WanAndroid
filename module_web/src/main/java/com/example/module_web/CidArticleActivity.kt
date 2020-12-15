package com.example.module_web

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_net.loadAction
import com.example.module_web.databinding.ActivityCidArticleBinding
import com.example.module_web.remote.ArticleCidService
import com.example.module_web.remote.ProjectCidService
import com.example.module_web.remote.PublicCidService
import com.example.module_web.repository.CidArticleRepository
import com.example.share_article.adapter.ArticleRecyclerViewAdapter
import com.example.share_article.request
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

@Route(path = "/cid/activity")
class CidArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCidArticleBinding
    private val adapter by inject<ArticleRecyclerViewAdapter> { parametersOf(false) }
    private val repository by inject<CidArticleRepository> {
        parametersOf(api())
    }

    @Autowired(name = "cid")
    lateinit var cid: String

    @Autowired(name = "cate")
    lateinit var cate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cid_article)

        ARouter.getInstance().inject(this)

        initView()
        initAction()
        subscribe()

    }

    private fun initView() {
        setSupportActionBar(binding.webCidArticleToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.webCidArticleRv.adapter = adapter
        binding.webCidArticleSrl.isRefreshing = true
        refresh()
    }

    private fun initAction() {
        binding.webCidArticleSrl.setOnRefreshListener {
            refresh()
        }
        binding.webCidArticleRv.loadAction {
            load()
        }
        binding.webCidArticleFabUp.fabUp.setOnClickListener {
            binding.webCidArticleRv.smoothScrollToPosition(0)
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
        repository.refresh(cid.toInt()).request(
            start = null,
            completion = { binding.webCidArticleSrl.isRefreshing = false }
        ) {
            adapter.submitList(it)
        }
    }

    private fun load() {
        repository.load(cid.toInt()).request(
            start = { binding.webCidArticleLoad.root.visibility = View.VISIBLE },
            completion = { binding.webCidArticleLoad.root.visibility = View.INVISIBLE }
        ) {
            val cur = adapter.currentList.toMutableList()
            cur.addAll(it)
            adapter.submitList(cur)
        }
    }

    private fun api() = when (cate) {
        "project" -> get<ProjectCidService>()
        "public" -> get<PublicCidService>()
        else -> get<ArticleCidService>()
    }
}