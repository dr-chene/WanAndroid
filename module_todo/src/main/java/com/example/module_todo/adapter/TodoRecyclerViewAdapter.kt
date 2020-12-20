package com.example.module_todo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.module_todo.bean.Todo
import com.example.module_todo.databinding.RecycleItemTodoBinding
import org.koin.java.KoinJavaComponent.get

/**
 *Created by chene on 20-12-20
 */
class TodoRecyclerViewAdapter(
    private val option: (Todo, View) -> Unit
) : ListAdapter<Todo, RecyclerView.ViewHolder>(get(Todo.TodoDiffCallBack::class.java)) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TodoViewHolder(
            RecycleItemTodoBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TodoViewHolder).bind(getItem(position), option)
    }

    class TodoViewHolder(
        private val binding: RecycleItemTodoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: Todo, option: (Todo, View) -> Unit) {
            binding.todo = todo
            binding.todoRecycleItemMore.setOnClickListener {
                option.invoke(todo, it)
            }
            binding.executePendingBindings()
        }
    }
}