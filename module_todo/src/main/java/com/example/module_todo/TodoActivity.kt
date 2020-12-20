package com.example.module_todo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.showToast
import com.example.lib_base.view.BaseActivity
import com.example.lib_net.loadAction
import com.example.lib_net.result
import com.example.module_todo.adapter.TodoRecyclerViewAdapter
import com.example.module_todo.bean.Todo
import com.example.module_todo.databinding.TodoActivityBinding
import com.example.module_todo.fragment.SortDialogFragment
import com.example.module_todo.viewmodel.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

@Route(path = "/todo/activity")
class TodoActivity : BaseActivity() {

    private lateinit var binding: TodoActivityBinding
    private val sortFragment by inject<SortDialogFragment>()
    private val todoViewModel by inject<TodoViewModel>()
    private val adapter by inject<TodoRecyclerViewAdapter> { parametersOf(optionMenuClick) }
    private val optionMenuClick: (Todo, View) -> Unit = { todo, view ->
        optionShow(todo, view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.todo_activity)

        initView()
        initAction()
        subscribe()
    }

    override fun onResume() {
        super.onResume()
        refresh()
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
                sortFragment.show(supportFragmentManager, "sort")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initView() {
        setSupportActionBar(binding.todoToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.todoRv.adapter = adapter
    }

    private fun initAction() {
        binding.todoSrl.setOnRefreshListener {
            todoViewModel.refresh {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.todoSrl.isRefreshing = false
                }
            }
        }
        binding.todoRv.loadAction {
            todoViewModel.load(adapter.currentList, {}) {
                CoroutineScope(Dispatchers.Main).launch {
                    "load data success".showToast()
                }
            }
        }
        binding.todoFabAdd.setOnClickListener {
            add()
        }
    }

    private fun refresh() {
        binding.todoSrl.isRefreshing = true
        todoViewModel.refresh {
            binding.todoSrl.isRefreshing = false
        }
    }

    private fun subscribe() {
        todoViewModel.todos.observe(this) {
            adapter.submitList(it)
        }
        todoViewModel.refreshing.observe(this){
            binding.todoSrl.isRefreshing = it
        }
    }

    private fun optionShow(todo: Todo, view: View) {
        PopupMenu(this, view).apply {
            menuInflater.inflate(R.menu.todo_menu_options, menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.todo_menu_options_item_delete -> delete(todo.id)
                    R.id.todo_menu_options_item_modify -> modify(todo)
                    R.id.todo_menu_options_item_complete -> complete(todo.id, STATUS_COMPLETE)
                    R.id.todo_menu_options_item_un_complete -> complete(todo.id, STATUS_UN_COMPLETE)
                }
                return@setOnMenuItemClickListener false
            }
            show()
        }
    }

    private fun add() {
        todoViewModel.modifyTodo = null
        startActivity(Intent(this, TodoAddActivity::class.java))
    }

    private fun complete(id: Int, status: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            todoViewModel.complete(id, status).result(null, null) {
                refresh()
                "待办事项修改成功".showToast()
            }
        }
    }

    private fun modify(todo: Todo) {
        todoViewModel.modifyTodo = todo
        startActivity(Intent(this, TodoAddActivity::class.java))
    }

    private fun delete(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            todoViewModel.delete(id).result(null, null) {
                refresh()
                "待办事项删除成功".showToast()
            }
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