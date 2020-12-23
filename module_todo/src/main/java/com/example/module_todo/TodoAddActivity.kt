package com.example.module_todo

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.lib_base.showToast
import com.example.lib_net.result
import com.example.module_todo.TodoActivity.Companion.PRIORITY_IMPORTANT
import com.example.module_todo.TodoActivity.Companion.PRIORITY_NORMAL
import com.example.module_todo.TodoActivity.Companion.TYPE_LIFE
import com.example.module_todo.TodoActivity.Companion.TYPE_PLAY
import com.example.module_todo.TodoActivity.Companion.TYPE_WORK
import com.example.module_todo.adapter.toPriority
import com.example.module_todo.adapter.toType
import com.example.module_todo.databinding.ActivityTodoAddBinding
import com.example.module_todo.viewmodel.TodoViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*

class TodoAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoAddBinding
    private val todoViewModel by inject<TodoViewModel>()
    private val todo = todoViewModel.modifyTodo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_todo_add)

        initView()
        initAction()
    }

    private fun initView() {
        setSupportActionBar(binding.todoAddToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (todo != null) binding.apply {
            todoAddTitle.editText?.setText(todo.title)
            todoAddContent.editText?.setText(todo.content)
            todoAddDate.editText?.setText(todo.dateStr)
            todoAddType.editText?.setText(todo.type.toType())
            todoAddPriority.editText?.setText(todo.priority.toPriority())
        }
        initType()
        initPriority()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        } else return super.onOptionsItemSelected(item)
    }

    private fun initAction() {
        binding.todoAddBtnDate.setOnClickListener {
            date()
        }
        binding.todoAddBtnAdd.setOnClickListener {
            if (todo == null) {
                add()
            } else {
                modify(todo.id, todo.status)
            }
        }
    }

    private fun modify(id: Int, status: Int) = commit { title, content, date, t, p ->
        CoroutineScope(Dispatchers.IO).launch {
            todoViewModel.modify(id, title, content, date, status, t, p).result(null) {
                this@TodoAddActivity.finish()
                "待办事项修改成功".showToast()
            }
        }
    }

    private fun add() = commit { title, content, date, t, p ->
        CoroutineScope(Dispatchers.IO).launch {
            todoViewModel.add(title, content, date, t, p).result(null) {
                this@TodoAddActivity.finish()
                "待办事项添加成功".showToast()
            }
        }
    }

    private fun commit(action: (String, String, String, Int, Int) -> Unit) {
        val title = binding.todoAddTitle.editText?.text.toString()
        val content = binding.todoAddContent.editText?.text.toString()
        val date = binding.todoAddDate.editText?.text.toString()
        val t = binding.todoAddType.editText?.text.toString()
        val p = binding.todoAddPriority.editText?.text.toString()
        if (title.isEmpty() || content.isEmpty() || date.isEmpty()) {
            "请输入title、content或date".showToast()
        } else {
            action.invoke(title, content, date, type(t), priority(p))
        }
    }

    private fun date() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()
        picker.apply {
            addOnPositiveButtonClickListener {
                val date = Date(it)
                val sd = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
                binding.todoAddDate.editText?.setText(sd.format(date))
            }
            show(supportFragmentManager, picker.toString())
        }
    }

    private fun initType() {
        (binding.todoAddType.editText as? AutoCompleteTextView)?.setAdapter(
            ArrayAdapter(
                this,
                R.layout.list_item,
                listOf("生活", "工作", "娱乐")
            )
        )
    }

    private fun initPriority() {
        (binding.todoAddPriority.editText as? AutoCompleteTextView)?.setAdapter(
            ArrayAdapter(
                this,
                R.layout.list_item,
                listOf("普通", "重要")
            )
        )
    }

    private fun type(type: String) =
        when (type) {
            "生活" -> TYPE_LIFE
            "工作" -> TYPE_WORK
            "娱乐" -> TYPE_PLAY
            else -> TYPE_LIFE
        }

    private fun priority(priority: String) =
        when (priority) {
            "普通" -> PRIORITY_NORMAL
            "重要" -> PRIORITY_IMPORTANT
            else -> PRIORITY_NORMAL
        }
}