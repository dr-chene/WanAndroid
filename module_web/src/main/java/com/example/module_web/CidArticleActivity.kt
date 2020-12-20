package com.example.module_web

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.showToast
import com.example.lib_net.loadAction
import com.example.lib_net.request
import com.example.lib_net.result
import com.example.module_web.databinding.ActivityCidArticleBinding
import com.example.module_web.remote.ArticleCidService
import com.example.module_web.remote.ProjectCidService
import com.example.module_web.remote.PublicCidService
import com.example.module_web.repository.CidArticleRepository
import com.example.module_web.repository.SearchCidArticleRepository
import com.example.module_web.repository.UserShareArticleRepository
import com.example.share_article.adapter.ArticleRecyclerViewAdapter
import com.example.share_collect.view.ShareArticleDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

@Route(path = "/cid/activity")
class CidArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCidArticleBinding
    private val delete: (Int) -> Unit = {
        CoroutineScope(Dispatchers.IO).launch {
            shareArticleRepo.delete(it).request().result(null, null) {
                binding.webCidArticleSrl.isRefreshing = true
                refresh()
                "分享文章删除成功".showToast()
            }
        }
    }
    private val adapter by inject<ArticleRecyclerViewAdapter> {
        parametersOf(
            false,
            cate == "myShare",
            if (cate != "myShare") null else delete
        )
    }
    private val repository by inject<CidArticleRepository> {
        parametersOf(api())
    }
    private val searchRepository by inject<SearchCidArticleRepository>()
    private var searchQuery: String = ""
    private val shareArticleRepo by inject<UserShareArticleRepository> { parametersOf(cate == "myShare") }

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

    }

    private fun initView() {
        setSupportActionBar(binding.webCidArticleToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.webCidArticleRv.adapter = adapter
        if (cate == "share" || cate == "myShare") {
            binding.articleShareUserCoin.root.visibility = View.VISIBLE
            binding.articleShareUserCoin.myRankUserHead.visibility = View.VISIBLE
        }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (cate == "public") return initSearchMenu(menu)
        else if (cate == "myShare") {
            menuInflater.inflate(R.menu.web_share_article, menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_item_share_article -> {
                ShareArticleDialog(this).apply {
                    setOnDismissListener {
                        binding.webCidArticleSrl.isRefreshing = true
                        refresh()
                    }
                    show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun refresh() = CoroutineScope(Dispatchers.Main).launch {
        if (cate == "public" && searchQuery.isNotEmpty()) {
            searchRepository.refresh(searchQuery, cid.toInt())
        } else if (cate == "share" || cate == "myShare") {
            shareArticleRepo.refresh(cid.toInt())
        } else {
            repository.refresh(cid.toInt())
        }.result(
            start = null,
            completion = { binding.webCidArticleSrl.isRefreshing = false }
        ) {
            adapter.submitList(it?.datas)
            if (cate == "share" || cate == "myShare") {
                binding.articleShareUserCoin.coin = shareArticleRepo.userCoin?.data
                binding.executePendingBindings()
            }
        }
    }

    private fun load() = CoroutineScope(Dispatchers.Main).launch {
        if (cate == "public" && searchQuery.isNotEmpty()) {
            searchRepository.load(cid.toInt(), searchQuery)
        } else if (cate == "share" || cate == "myShare0") {
            shareArticleRepo.load(cid.toInt())
        } else {
            repository.load(cid.toInt())
        }.result(
            start = { binding.webCidArticleLoad.root.visibility = View.VISIBLE },
            completion = { binding.webCidArticleLoad.root.visibility = View.INVISIBLE }
        ) {
            val cur = adapter.currentList.toMutableList()
            it?.datas?.let { list -> cur.addAll(list) }
            adapter.submitList(cur)
        }
    }

    private fun api() = when (cate) {
        "project" -> get<ProjectCidService>()
        "public" -> get<PublicCidService>()
        else -> get<ArticleCidService>()
    }

    private fun initSearchMenu(menu: Menu?): Boolean {
        menu ?: return true

        menuInflater.inflate(R.menu.web_search_menu, menu)
        (menu.findItem(R.id.web_public_search).actionView as SearchView).apply {
            findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text).let {
                it.background = ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.toolbar_searchview_background,
                    theme
                )
                it.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                it.hint = "在公众号中查找..."
                it.setHintTextColor(Color.GRAY)
                it.setTextColor(Color.BLACK)
            }
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    search(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty()) searchQuery = ""
                    return false
                }
            })
            setOnCloseListener {
                searchQuery = ""
                return@setOnCloseListener false
            }
        }
        return true
    }

    private fun search(query: String?) {
        query ?: return
        searchQuery = query
        binding.webCidArticleSrl.isRefreshing = true
        refresh()
    }
}