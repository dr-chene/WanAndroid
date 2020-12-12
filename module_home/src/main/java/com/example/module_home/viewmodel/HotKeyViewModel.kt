package com.example.module_home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lib_net.bean.NetResult

import com.example.module_home.repository.HotKeyRepository
import com.example.share_article.bean.HotKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

/**
Created by chene on @date 20-12-6 上午9:37
 **/
class HotKeyViewModel(
    private val hotKeyRepository: HotKeyRepository
) : ViewModel() {

    val hotKeys: LiveData<List<HotKey>>
        get() = _hotKeys
    private val _hotKeys = MutableLiveData<List<HotKey>>()

    @ExperimentalCoroutinesApi
    suspend fun getHotKey() {
        hotKeyRepository.getHotKey().collectLatest {
            if (it is NetResult.Success) {
                _hotKeys.postValue(it.value)
            }
        }
    }
}