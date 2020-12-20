package com.example.module_todo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.module_todo.databinding.FragmentSortBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 *Created by chene on 20-12-20
 */
class SortDialogFragment: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSortBinding

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
        binding.todoSortConfirm.setOnClickListener {
            this.dismiss()
        }
    }
}