package com.example.module_todo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.view.BaseActivity
import com.example.module_todo.databinding.TodoActivityBinding
import com.example.module_todo.fragment.SortDialogFragment
import com.example.module_todo.viewmodel.SortViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

@Route(path = "/todo/activity")
class TodoActivity : BaseActivity() {

    private lateinit var binding: TodoActivityBinding
    private val sortFragment by inject<SortDialogFragment>()
    private val sortViewModel by viewModel<SortViewModel>()
    private var

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.todo_activity)

        initView()
        initAction()
        subscribe()
    }

    private fun subscribe() {

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

    private fun initAction() {
        setSupportActionBar(binding.todoToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initView() {

    }

}