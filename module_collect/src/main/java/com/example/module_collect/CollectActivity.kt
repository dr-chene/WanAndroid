package com.example.module_collect

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lib_base.view.BaseActivity
import com.example.lib_net.loadAction
import com.example.module_collect.databinding.CollectActivityBinding


abstract class CollectActivity : BaseActivity() {

    protected lateinit var binding: CollectActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.collect_activity)

        initView()
        initAction()
    }

    private fun initView() {
        setSupportActionBar(binding.collectToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initRecyclerView(binding.collectRv)
        binding.collectSrl.isRefreshing = true
        refresh()
    }

    protected abstract fun initRecyclerView(recyclerView: RecyclerView)

    private fun initAction() {
        binding.collectSrl.setOnRefreshListener {
            refresh()
        }
        binding.collectRv.loadAction {
            load()
        }
        binding.collectFabUp.fabUp.setOnClickListener {
            binding.collectRv.smoothScrollToPosition(0)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.collect_add -> {
                add()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    abstract fun add()

    protected abstract fun refresh()

    protected abstract fun load()
}