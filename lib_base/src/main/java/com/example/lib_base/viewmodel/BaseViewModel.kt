package com.example.lib_base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    val refreshing: LiveData<Boolean>
        get() = _refreshing
    protected val _refreshing = MutableLiveData<Boolean>()

    val loading: LiveData<Boolean>
        get() = _loading
    protected val _loading = MutableLiveData<Boolean>()
}