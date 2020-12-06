package com.example.module_home.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lib_base.HotKey
import com.example.lib_base.netWorkCheck
import com.example.module_home.repository.HotKeyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import org.koin.java.KoinJavaComponent.get

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
        if (netWorkCheck()) {
            hotKeyRepository.getHotKey().collectLatest {
                _hotKeys.postValue(it.data.sortedBy { hotKey ->
                    hotKey.order
                })
            }
        } else {
            Toast.makeText(get(Context::class.java), "网络连接失败，请检查后重试...", Toast.LENGTH_SHORT).show()
        }
    }
}