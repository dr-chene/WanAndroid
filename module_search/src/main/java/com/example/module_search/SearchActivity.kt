package com.example.module_search

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.lib_base.HotKey
import com.example.lib_base.view.BaseActivity
import com.example.module_search.databinding.ActivitySearchBinding

@Route(path = "/search/activity")
class SearchActivity : BaseActivity() {

    @Autowired(name = "hotkeys")
    lateinit var hotKeys: List<HotKey>

    private lateinit var searchBinding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        makeStatusBarTransparent()
        makeStatusBarIconDark()
        super.onCreate(savedInstanceState)
        searchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        ARouter.getInstance().inject(this)

    }
}