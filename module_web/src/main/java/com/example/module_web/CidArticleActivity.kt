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
import com.example.module_web.viewmodel.CidArticleViewModel
import com.example.module_web.viewmodel.SearchCidArticleViewModel
import com.example.module_web.viewmodel.UserShareArticleViewModel
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
            userShareArticleViewModel.delete(it).request().result(null) {
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
    private val viewModel by inject<CidArticleViewModel> {
        parametersOf(api())
    }
    private val cidArticleViewModel by inject<SearchCidArticleViewModel>()
    private var searchQuery: String = ""
    private val userShareArticleViewModel by inject<UserShareArticleViewModel> { parametersOf(cate == "myShare") }

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

    private fun subscribe() {
        viewModel().articles.observe(this){
            adapter.submitList(it)
            if (cate == "share" || cate == "myShare") {
                binding.articleShareUserCoin.coin = userShareArticleViewModel.userCoin?.data
                binding.executePendingBindings()
            }
        }
        viewModel().refreshing.observe(this){
            binding.webCidArticleSrl.isRefreshing = it
        }
        viewModel().loading.observe(this){
            binding.webCidArticleLoad.root.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
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
            cidArticleViewModel.refresh(searchQuery, cid.toInt())
        } else if (cate == "share" || cate == "myShare") {
            userShareArticleViewModel.refresh(cid.toInt())
        } else {
            viewModel.refresh(cid.toInt())
        }
    }

    private fun load() = CoroutineScope(Dispatchers.Main).launch {
        if (cate == "public" && searchQuery.isNotEmpty()) {
            cidArticleViewModel.load(cid.toInt(), searchQuery, adapter.currentList.toMutableList())
        } else if (cate == "share" || cate == "myShare") {
            userShareArticleViewModel.load(cid.toInt(), adapter.currentList.toMutableList())
        } else {
            viewModel.load(cid.toInt(), adapter.currentList.toMutableList())
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

    private fun viewModel() = if (cate == "public" && searchQuery.isNotEmpty()) {
        cidArticleViewModel
    } else if (cate == "share" || cate == "myShare") {
        userShareArticleViewModel
    } else {
        viewModel
    }
}