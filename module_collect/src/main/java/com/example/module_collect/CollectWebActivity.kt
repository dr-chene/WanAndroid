package com.example.module_collect

import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_net.request
import com.example.lib_net.result
import com.example.module_collect.adapter.CollectWebAdapter
import com.example.module_collect.remote.CollectWebService
import com.example.share_collect.bean.CollectWeb
import com.example.share_collect.view.CollectWebDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

/**
 *Created by chene on 20-12-19
 */
@Route(path = "/web_collect/activity")
class CollectWebActivity : CollectActivity() {

    private val api by inject<CollectWebService>()
    private val modify: (CollectWeb) -> Unit = {
        CollectWebDialog(this, it).apply {
            setOnDismissListener {
                binding.collectSrl.isRefreshing = true
                refresh()
            }
            show()
        }
    }
    private val adapter by inject<CollectWebAdapter> { parametersOf(modify) }

    override fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.collect_menu_web, menu)
        return true
    }

    override fun add() {
        CollectWebDialog(this).apply {
            setOnDismissListener {
                binding.collectSrl.isRefreshing = true
                refresh()
            }
            show()
        }
    }

    override fun refresh() {
        CoroutineScope(Dispatchers.Main).launch {
            api.getWebs().request().result(
                start = null,
                completion = {
                    binding.collectSrl.isRefreshing = false
                }
            ) {
                adapter.submitList(it)
            }
        }
    }

    override fun load() {}
}