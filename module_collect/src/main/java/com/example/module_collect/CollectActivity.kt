package com.example.module_collect

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lib_base.view.BaseActivity
import com.example.lib_base.viewmodel.BaseViewModel
import com.example.lib_net.loadAction
import com.example.module_collect.databinding.CollectActivityBinding


abstract class CollectActivity(
    private val viewModel: BaseViewModel
) : BaseActivity() {

    protected lateinit var binding: CollectActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.collect_activity)

        initView()
        initAction()
        subscribe()
    }

    private fun subscribe() {
        submitList()
        viewModel.loading.observe(this) {
            binding.collectLoad.root.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
        viewModel.refreshing.observe(this) {
            binding.collectSrl.isRefreshing = it
        }
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

    protected abstract fun add()

    protected abstract fun refresh()

    protected abstract fun load()

    protected abstract fun submitList()
}