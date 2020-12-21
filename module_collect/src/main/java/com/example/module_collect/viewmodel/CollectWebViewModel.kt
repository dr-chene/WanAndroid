package com.example.module_collect.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lib_base.viewmodel.BaseViewModel
import com.example.lib_net.request
import com.example.lib_net.result
import com.example.module_collect.remote.CollectWebService
import com.example.share_collect.bean.CollectWeb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CollectWebViewModel(
    private val api: CollectWebService
) : BaseViewModel() {

    val webs: LiveData<List<CollectWeb>>
        get() = _webs
    private val _webs = MutableLiveData<List<CollectWeb>>()

    fun refresh() {
        CoroutineScope(Dispatchers.IO).launch {
            api.getWebs().request().result(
                completion = { _refreshing.postValue(false) }
            ) {
                _webs.postValue(it)
            }
        }
    }
}