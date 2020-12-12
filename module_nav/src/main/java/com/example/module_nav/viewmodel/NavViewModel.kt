package com.example.module_nav.viewmodel

import androidx.lifecycle.ViewModel
import com.example.module_nav.repository.NavRepository

/**
Created by chene on @date 20-12-12 下午4:29
 **/
class NavViewModel internal constructor(
    private val navRepository: NavRepository
) : ViewModel() {

    fun getNavs() = navRepository.local()

    suspend fun refresh() = navRepository.refresh()
}