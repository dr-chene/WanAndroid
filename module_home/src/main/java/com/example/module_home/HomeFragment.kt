package com.example.module_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.showToast
import com.example.lib_base.view.BaseFragment
import com.example.module_home.adapter.MyBannerAdapter
import com.example.module_home.bean.Banner
import com.example.module_home.databinding.HomeFragmentBinding
import com.example.module_home.viewmodel.ArticleViewModel
import com.example.module_home.viewmodel.BannerViewModel
import com.example.module_home.viewmodel.HotKeyViewModel
import com.example.share_article.adapter.ArticleRecyclerViewAdapter
import com.example.share_article.bean.HotKey
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

@Route(path = "/home/fragment")
class HomeFragment : BaseFragment() {

    private lateinit var homeBinding: HomeFragmentBinding
    private val articleViewModel by viewModel<ArticleViewModel>()
    private val bannerViewModel by viewModel<BannerViewModel>()
    private val hotKeyViewModel by viewModel<HotKeyViewModel>()
    private val articleAdapter by inject<ArticleRecyclerViewAdapter> { parametersOf(true, false, null) }
    private val bannerAdapter by inject<MyBannerAdapter> { parametersOf(listOf<Banner>()) }
    private var hotKeys: List<HotKey> = listOf()
    private var hotKeysAnim: Job? = null
    private var  first = true
    private var curY = 0f
    private var nextY = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = HomeFragmentBinding.inflate(inflater, container, false)
        context ?: return homeBinding.root

        initView()
        initAction()
        subscribe()

        return homeBinding.root
    }

    private fun initView() {
        homeBinding.homeSwipeRefresh.isRefreshing = true
        homeBinding.includeContent.homeRv.adapter = articleAdapter
        homeBinding.includeContent.homeBanner.apply {
            addBannerLifecycleObserver(viewLifecycleOwner)
            indicator = CircleIndicator(this.context)
            adapter = bannerAdapter
        }
        refreshArticle()
    }

    private fun initAction() {
        homeBinding.homeSwipeRefresh.setOnRefreshListener {
            refreshArticle()
        }
        homeBinding.includeContent.homeRv.isNestedScrollingEnabled = false
        (homeBinding.includeContent.root as NestedScrollView).setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
                if (scrollY == ((v as NestedScrollView).getChildAt(0)).measuredHeight - v.measuredHeight) {
                    loadArticle()
                }
                if (scrollY == 0) {
                    homeBinding.homeFabUp.root.visibility = View.INVISIBLE
                } else {
                    homeBinding.homeFabUp.root.visibility = View.VISIBLE
                }
            })
        //拦截点击事件
//        homeBinding.homeLoad.root.setOnClickListener { }
        homeBinding.homeFabUp.fabUp.setOnClickListener {
            (homeBinding.includeContent.root as NestedScrollView).smoothScrollTo(0, 0)
        }
        homeBinding.includeContent.includeSearchBar.searchBar.setOnClickListener {
            search()
        }
    }

    private fun subscribe() {
        articleViewModel.articles.observe(viewLifecycleOwner) {
            articleAdapter.submitList(it)
        }
        bannerViewModel.banners.observe(viewLifecycleOwner) {
            bannerAdapter.setDatas(it)
            bannerAdapter.notifyDataSetChanged()
        }
        hotKeyViewModel.hotKeys.observe(viewLifecycleOwner) {
            hotKeys = it
            if (first) {
                curY = homeBinding.includeContent.includeSearchBar.tvCur.y
                nextY = homeBinding.includeContent.includeSearchBar.tvNext.y
                first = false

            }
            hotKeysAnim?.cancel()
            hotKeysAnim = changeHotWord(hotKeys)
        }
    }

    private fun refreshArticle() = CoroutineScope(Dispatchers.Main).launch {
        loadBanner()
        loadHotKey()
        articleViewModel.refreshArticle(
            start = {},
            end = {
                loadDataSuccess()
                homeBinding.homeSwipeRefresh.isRefreshing = false
                homeBinding.homeLoad.root.visibility = View.GONE
                cancel()
            }
        ) {
            it?.showToast()
            loadDataError.invoke()
        }
    }

    private fun loadArticle() = CoroutineScope(Dispatchers.Main).launch {
        articleViewModel.loadArticle(
            start = {
                homeBinding.homeLoad.root.visibility = View.VISIBLE
            },
            end = {
                homeBinding.homeLoad.root.visibility = View.GONE
                cancel()
            }
        ) {
            it?.showToast()
            loadDataError.invoke()
        }
    }

    private val loadDataError: () -> Unit = {
        homeBinding.homeSwipeRefresh.isRefreshing = false
        homeBinding.includeContent.apply {
            homeRv.visibility = View.INVISIBLE
            ivLoadError.visibility = View.VISIBLE
            tvLoadError.visibility = View.VISIBLE
        }
    }

    private val loadDataSuccess: () -> Unit = {
        homeBinding.includeContent.apply {
            homeRv.visibility = View.VISIBLE
            ivLoadError.visibility = View.INVISIBLE
            tvLoadError.visibility = View.INVISIBLE
        }
    }

    private suspend fun loadBanner() = withContext(Dispatchers.IO) {
        bannerViewModel.loadBanner()
    }


    private suspend fun loadHotKey() = withContext(Dispatchers.IO) {
        hotKeyViewModel.getHotKey()
    }


    private fun changeHotWord(hotKeys: List<HotKey>) = CoroutineScope(Dispatchers.Main).launch {
        var index = 0
        if (hotKeys.isNotEmpty()) homeBinding.includeContent.includeSearchBar.apply {
            val len = hotKeys.size
            while (true) {
                val curText = hotKeys[index++ % len].name
                val distance = nextY - curY
                val nextTextView = if (tvCur.y < tvNext.y) tvNext else tvCur
                val curTextView = if (tvCur.y > tvNext.y) tvNext else tvCur
                curTextView.text = curText
                delay(HOT_WORD_CHANGE_DURATION)
                nextTextView.change(nextY, curY, curY)
                curTextView.change(curY, curY - distance, nextY)
                nextTextView.y = curY
                curTextView.y = nextY
            }
        }
    }

    private fun search() {
        val cur = homeBinding.includeContent.includeSearchBar.let {
            if (it.tvCur.y < it.tvNext.y) it.tvCur else it.tvNext
        }
        ARouter.getInstance().build("/search/activity")
            .withTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_from_bottom)
            .withObject("hotkeys", hotKeys)
            .withString("hotkey", cur.text.toString())
            .navigation()
    }

    companion object {
        private const val HOT_WORD_CHANGE_DURATION = 3000L
    }
}