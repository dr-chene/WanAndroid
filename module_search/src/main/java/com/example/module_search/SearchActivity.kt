package com.example.module_search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.view.BaseActivity
import com.example.module_search.bean.SearchHistory
import com.example.module_search.databinding.SearchActivityBinding
import com.example.module_search.fragment.SearchedFragment
import com.example.module_search.viewmodel.SearchActivityViewModel
import com.example.share_article.bean.HotKey
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

@Route(path = "/search/activity")
class SearchActivity : BaseActivity() {

    @Autowired(name = "hotkeys")
    lateinit var hotKeys: List<HotKey>

    @Autowired(name = "hotkey")
    lateinit var hotKey: String

    private lateinit var searchBinding: SearchActivityBinding
    private val searchViewModel by viewModel<SearchActivityViewModel>()
    private var searchTag = SearchHistory.SEARCH_TAG_KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        makeStatusBarTransparent()
        makeStatusBarIconDark()
        super.onCreate(savedInstanceState)
        searchBinding = DataBindingUtil.setContentView(this, R.layout.search_activity)

        ARouter.getInstance().inject(this)

        initView()
        initAction()
        subscribe()
    }

    private fun initView() {
        searchViewModel.setHotKeys(hotKeys.sortedBy { it.id })
    }

    private fun initAction() {
        searchBinding.searchBar.searchBar.apply {
            hint = hotKey
            requestFocus()
            setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val key = if (v.text.isNullOrEmpty()) {
                        v.hint
                    } else v.text
                    searchViewModel.search(key.toString(), searchTag)
                    super.hideInput()
                }
                return@setOnEditorActionListener false
            }
            addTextChangedListener {
                if (it != null && it.toString().isNotEmpty()) {
                    searchBinding.searchBar.ivSearchBarCancel.visibility = View.VISIBLE
                } else {
                    searchBinding.searchBar.ivSearchBarCancel.visibility = View.INVISIBLE
                }
            }
        }
        searchBinding.searchBar.searchBarIvBack.setOnClickListener {
            onBackPressed()
        }
        searchBinding.searchBar.ivSearchBarCancel.setOnClickListener {
            searchBinding.searchBar.searchBar.setText("")
            onBackPressed()
        }
    }

    private fun subscribe() {
        searchViewModel.searchContent.observe(this) {
            searchBinding.searchBar.searchBar.apply {
                setText(it)
                setSelection(it.length)
            }
            super.hideInput()
            navToSearched()
        }
        searchViewModel.searching.observe(this) {
            searchBinding.searchSearching.root.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
        searchViewModel.searchTag.observe(this) {
            searchTag = it
        }
    }

    private fun navToSearched() {
        supportFragmentManager.commit {
            addToBackStack("searched")
            replace(R.id.search_fragment_container,
                get<SearchedFragment> { parametersOf(searchTag) })
        }
    }
}