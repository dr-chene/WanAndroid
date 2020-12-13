package com.example.module_web

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.view.BaseActivity
import com.example.module_web.databinding.WebActivityBinding

@Route(path = "/web/activity")
class WebActivity : BaseActivity() {

    private lateinit var webActivityBinding: WebActivityBinding

    @Autowired(name = "link")
    lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webActivityBinding = DataBindingUtil.setContentView(this, R.layout.web_activity)

        ARouter.getInstance().inject(this)

        initView()
        initAction()
        subscribe()
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
        webActivityBinding.webFabUp.setOnClickListener {
            webActivityBinding.webNestScrollView.smoothScrollTo(0, 0)
            webActivityBinding.webAppbar.setExpanded(true)
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
            R.id.menu_item_browser -> {
                browser()
                true
            }
            R.id.menu_item_share -> {
                share()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.web_menu, menu)
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