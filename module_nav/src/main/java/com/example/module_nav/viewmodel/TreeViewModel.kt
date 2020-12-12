package com.example.module_nav.viewmodel

import androidx.lifecycle.ViewModel
import com.example.module_nav.repository.TreeRepository

/**
Created by chene on @date 20-12-12 下午8:47
 **/
class TreeViewModel internal constructor(
    private val treeRepository: TreeRepository
): ViewModel() {

    fun getTrees() = treeRepository.load()

    suspend fun refresh() = treeRepository.refresh()
}