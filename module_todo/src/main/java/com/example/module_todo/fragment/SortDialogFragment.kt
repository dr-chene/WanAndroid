package com.example.module_todo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.module_todo.R
import com.example.module_todo.TodoActivity.Companion.ORDERBY_COMPLETE
import com.example.module_todo.TodoActivity.Companion.ORDERBY_COMPLETE_DESC
import com.example.module_todo.TodoActivity.Companion.ORDERBY_CREATE
import com.example.module_todo.TodoActivity.Companion.ORDERBY_CREATE_DESC
import com.example.module_todo.TodoActivity.Companion.PRIORITY_IMPORTANT
import com.example.module_todo.TodoActivity.Companion.PRIORITY_NORMAL
import com.example.module_todo.TodoActivity.Companion.STATUS_COMPLETE
import com.example.module_todo.TodoActivity.Companion.STATUS_UN_COMPLETE
import com.example.module_todo.TodoActivity.Companion.TYPE_LIFE
import com.example.module_todo.TodoActivity.Companion.TYPE_PLAY
import com.example.module_todo.TodoActivity.Companion.TYPE_WORK
import com.example.module_todo.databinding.FragmentSortBinding
import com.example.module_todo.viewmodel.SortViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject

/**
 *Created by chene on 20-12-20
 */
class SortDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSortBinding
    private val sortViewModel by inject<SortViewModel>()
    private var status: Int? = null
    private var type: Int? = null
    private var priority: Int? = null
    private var orderby: Int = 4

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSortBinding.inflate(inflater, container, false)
        context ?: return binding.root

        initView()
        initAction()

        return binding.root
    }

    private fun initView() {
        binding.todoSortStatusAll.isChecked = true
        binding.todoSortTypeAll.isChecked = true
        binding.todoSortPriorityAll.isChecked = true
        binding.todoSortOrderbyCreateDesc.isChecked = true
    }

    private fun initAction() {
        binding.todoSortStatusChips.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.todo_sort_status_complete -> status = STATUS_COMPLETE
                R.id.todo_sort_status_un_complete -> status = STATUS_UN_COMPLETE
                R.id.todo_sort_status_all -> status = null
            }
        }
        binding.todoSortTypeChips.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.todo_sort_type_life -> type = TYPE_LIFE
                R.id.todo_sort_type_work -> type = TYPE_WORK
                R.id.todo_sort_type_play -> type = TYPE_PLAY
                R.id.todo_sort_type_all -> type = null
            }
        }
        binding.todoSortPriorityChips.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.todo_sort_priority_important -> priority = PRIORITY_IMPORTANT
                R.id.todo_sort_priority_normal -> priority = PRIORITY_NORMAL
                R.id.todo_sort_priority_all -> priority = null
            }
        }
        binding.todoSortOrderbyChips.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.todo_sort_oderby_complete -> orderby = ORDERBY_COMPLETE
                R.id.todo_sort_orderby_create_desc -> orderby = ORDERBY_COMPLETE_DESC
                R.id.todo_sort_orderby_create -> orderby = ORDERBY_CREATE
                R.id.todo_sort_orderby_create_desc -> orderby = ORDERBY_CREATE_DESC
            }
        }
        binding.todoSortConfirm.setOnClickListener {
            sortViewModel.sort(status, type, priority, orderby)
            this.dismiss()
        }
    }
}