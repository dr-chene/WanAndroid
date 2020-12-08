package com.example.module_home

import android.accounts.NetworkErrorException
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.HotKey
import com.example.lib_base.showToast
import com.example.lib_base.view.BaseFragment
import com.example.module_home.adapter.ArticleRecyclerViewAdapter
import com.example.module_home.adapter.MyBannerAdapter
import com.example.module_home.bean.Banner
import com.example.module_home.databinding.FragmentHomeBinding
import com.example.module_home.viewmodel.ArticleViewModel
import com.example.module_home.viewmodel.BannerViewModel
import com.example.module_home.viewmodel.HotKeyViewModel
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.max
import kotlin.math.min

@Route(path = "/home/fragment")
class HomeFragment : BaseFragment() {

    private lateinit var homeBinding: FragmentHomeBinding
    private val articleViewModel by viewModel<ArticleViewModel>()
    private val bannerViewModel by viewModel<BannerViewModel>()
    private val hotKeyViewModel by viewModel<HotKeyViewModel>()
    private val articleAdapter by inject<ArticleRecyclerViewAdapter>()
    private val bannerAdapter by inject<MyBannerAdapter> { parametersOf(listOf<Banner>()) }
    private var hotKeys: List<HotKey> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
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
        loadHotKey()
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
                    homeBinding.fabUp.visibility = View.INVISIBLE
                } else {
                    homeBinding.fabUp.visibility = View.VISIBLE
                }
            })
        homeBinding.loadMore.root.setOnClickListener { }
        homeBinding.fabUp.setOnClickListener {
            (homeBinding.includeContent.root as NestedScrollView).smoothScrollTo(0, 0)
        }
        homeBinding.includeContent.includeSearchBar.searchBar.setOnClickListener {
            search()
        }
    }

    private fun subscribe() {
        articleViewModel.articles.observe(viewLifecycleOwner) {
            articleAdapter.submitList(it)
            loadDataSuccess()
            homeBinding.homeSwipeRefresh.isRefreshing = false
            homeBinding.loadMore.root.visibility = View.GONE
        }
        bannerViewModel.banners.observe(viewLifecycleOwner) {
            bannerAdapter.setDatas(it)
            bannerAdapter.notifyDataSetChanged()
        }
        hotKeyViewModel.hotKeys.observe(viewLifecycleOwner) {
            hotKeys = it
            changeHotWord(it)
        }
    }

    private fun refreshArticle() = load {
        CoroutineScope(Dispatchers.Main).launch {
            loadBanner()
            articleViewModel.refreshArticle(loadDataError)
        }
    }

    private fun loadArticle() = load {
        homeBinding.loadMore.root.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.Main).launch {
            articleViewModel.loadArticle {
                homeBinding.loadMore.root.visibility = View.GONE
            }
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

    private fun loadBanner() = load {
        CoroutineScope(Dispatchers.Main).launch {
            bannerViewModel.loadBanner {}
        }
    }

    private fun loadHotKey() = load {
        CoroutineScope(Dispatchers.Main).launch {
            hotKeyViewModel.getHotKey {}
        }
    }

    private fun changeHotWord(hotKeys: List<HotKey>) = CoroutineScope(Dispatchers.Main).launch {
        var index = 0;
        if (hotKeys.isNotEmpty()) homeBinding.includeContent.includeSearchBar.apply {
            val len = hotKeys.size
            while (true) {
                val curText = hotKeys[index++ % len].name
                val curY = min(tvCur.y, tvNext.y)
                val nextY = max(tvCur.y, tvNext.y)
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

    private fun load(load: () -> Unit) {
        try {
            load()
        } catch (e: NetworkErrorException) {
            e.message?.showToast()
        } catch (e: Throwable) {
            "发生了未知错误，请及时向开发者反映".showToast()
            Log.d("TAG_EXCEPTION", "load:${e.message}")
        }
    }

    private fun search() {
        ARouter.getInstance().build("/search/activity")
            .withObject("hotkeys", hotKeys)
            .navigation()
    }

    companion object {
        private const val HOT_WORD_CHANGE_DURATION = 3000L
    }
}