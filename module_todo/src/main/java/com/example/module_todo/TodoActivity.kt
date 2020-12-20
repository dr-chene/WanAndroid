package com.example.module_todo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.showToast
import com.example.lib_base.view.BaseActivity
import com.example.lib_net.loadAction
import com.example.module_todo.adapter.TodoRecyclerViewAdapter
import com.example.module_todo.databinding.TodoActivityBinding
import com.example.module_todo.fragment.SortDialogFragment
import com.example.module_todo.viewmodel.SortViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

@Route(path = "/todo/activity")
class TodoActivity : BaseActivity() {

    private lateinit var binding: TodoActivityBinding
    private val sortFragment by inject<SortDialogFragment>()
    private val sortViewModel by inject<SortViewModel>()
    private val adapter by inject<TodoRecyclerViewAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.todo_activity)

        initView()
        initAction()
        subscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.todo_menu_sort, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.todo_menu_item_sort -> {
                sortFragment.show(supportFragmentManager, "dialog")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView() {
        setSupportActionBar(binding.todoToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.todoRv.adapter = adapter
        binding.todoSrl.isRefreshing = true
        sortViewModel.refresh {
            binding.todoSrl.isRefreshing = false
        }
    }

    private fun initAction() {
        binding.todoSrl.setOnRefreshListener {
            sortViewModel.refresh {
                binding.todoSrl.isRefreshing = false
            }
        }
        binding.todoRv.loadAction {
            sortViewModel.load(adapter.currentList, {}) {
                "load data success".showToast()
            }
        }
    }

    private fun subscribe() {
        sortViewModel.todos.observe(this){
            adapter.submitList(it)
        }
    }

    companion object {
        const val STATUS_COMPLETE = 1
        const val STATUS_UN_COMPLETE = 0
        const val TYPE_LIFE = 2
        const val TYPE_WORK = 1
        const val TYPE_PLAY = 3
        const val PRIORITY_IMPORTANT = 1
        const val PRIORITY_NORMAL = 2
        const val ORDERBY_COMPLETE = 1
        const val ORDERBY_COMPLETE_DESC = 2
        const val ORDERBY_CREATE = 3
        const val ORDERBY_CREATE_DESC = 4
    }
}