package com.example.module_web

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.showToast
import com.example.lib_base.view.BaseActivity
import com.example.lib_net.request
import com.example.lib_net.result
import com.example.module_web.databinding.WebActivityBinding
import com.example.share_collect.viewmodel.UnCollectArticleViewModel
import com.example.share_collect.viewmodel.ShareCollectViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent

@Route(path = "/web/activity")
class WebActivity : BaseActivity() {

    private lateinit var webActivityBinding: WebActivityBinding
    private val collectViewModel by inject<ShareCollectViewModel>()
    private val unCollectArticleViewModel by inject<UnCollectArticleViewModel>()
    private var collectMenuItem: MenuItem? = null

    @Autowired(name = "link")
    lateinit var url: String

    @Autowired(name = "collect")
    lateinit var collect: String

    @Autowired(name = "id")
    lateinit var id: String

    @Autowired(name = "originId")
    lateinit var originId: String

    @Autowired(name = "cate")
    lateinit var cate: String

    private var mCollect = false
    private var mId = 0
    private var mOriginId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webActivityBinding = DataBindingUtil.setContentView(this, R.layout.web_activity)

        ARouter.getInstance().inject(this)
        if (cate == "article") {
            mId = id.toInt()
            mCollect = collect.toBoolean()
            mOriginId = originId.toInt()
        }
        initView()
        initAction()
    }

    private fun initView() {
        setSupportActionBar(webActivityBinding.webToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //解决：加载部分网页空白
        webActivityBinding.webWeb.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }
        //解决：自动跳转浏览器
//        webActivityBinding.webWeb.webViewClient = object : WebViewClient(){
//            override fun onReceivedError(
//                view: WebView?,
//                request: WebResourceRequest?,
//                error: WebResourceError?
//            ) {
//                super.onReceivedError(view, request, error)
//                loadWebError()
//            }
//        }
        webActivityBinding.webWeb.loadUrl(url)
    }

    private fun initAction() {
        webActivityBinding.webFabUp.fabUp.setOnClickListener {
            webActivityBinding.webNestScrollView.smoothScrollTo(0, 0)
            webActivityBinding.webAppbar.setExpanded(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_item_browser -> {
                browser()
                true
            }
            R.id.menu_item_share -> {
                share()
                true
            }
            R.id.menu_item_collect -> {
                collect()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun collect() {
        synchronized(mCollect) {
            if (mCollect) {
                AlertDialog.Builder(KoinJavaComponent.get(Context::class.java)).apply {
                    setMessage("是否取消收藏?")
                    setPositiveButton("删除") { _, _ ->
                        unCollect()
                    }
                    setNegativeButton("取消操作") { _, _ ->
                        "操作取消".showToast()
                    }
                }.show().apply {
                    getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    collectViewModel.collectInnerArticle(mId).result(null) {
                        mCollect = true
                        collectMenuItem?.title = resources.getString(R.string.web_menu_un_collect)
                        "收藏成功".showToast()
                    }
                }
            }
        }
    }

    private fun unCollect() = CoroutineScope(Dispatchers.IO).launch {
        unCollectArticleViewModel.unCollect(mId, mOriginId).request().result(null) {
            mCollect = false
            collectMenuItem?.title = resources.getString(R.string.web_menu_collect)
            "取消收藏成功".showToast()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            if (cate == "web") R.menu.web_menu_web
            else if (mCollect) R.menu.web_menu_un_collect
            else R.menu.web_menu_collect,
            menu
        )
        collectMenuItem = menu?.findItem(R.id.menu_item_collect)
        return true
    }

    private fun browser() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun share() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/url"
        }
        startActivity(Intent.createChooser(shareIntent, "分享至"))
    }

//    private fun loadWebError() {
//        AlertDialog.Builder(this).apply {
//            setMessage("web加载异常，是否用浏览器打开？")
//            setPositiveButton("yes") { _, _ ->
//                browser()
//            }
//            setNegativeButton("no"){ _, _ ->
//                "操作取消".showToast()
//            }
//        }
//    }
}