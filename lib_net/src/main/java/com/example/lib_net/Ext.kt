package com.example.lib_net

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.lib_base.showToast
import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetResult
import com.example.lib_net.bean.doFailure
import com.example.lib_net.bean.doSuccess
import com.example.lib_net.util.MmkvUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

/**
Created by chene on @date 20-12-11 下午8:44
 **/
fun RecyclerView.isSlideToBottom() =
    (this.computeVerticalScrollExtent() + this.computeVerticalScrollOffset()
            >= this.computeVerticalScrollRange())

fun RecyclerView.loadAction(load: () -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING && recyclerView.isSlideToBottom()) {
                load.invoke()
            }
        }
    })
}

fun loginCheck(isLogin: () -> Unit) {
    if (MmkvUtil.isLogin()) {
        isLogin.invoke()
    } else {
        "请先登录！".showToast()
    }
}

private const val SUCCESS = 0
fun <T> NetBean<T>.request() = flow {
    try {
        if (this@request.errorCode == SUCCESS) {
            emit(NetResult.Success(this@request.data))
        } else {
            emit(NetResult.Failure(this@request.errorMsg))
        }
    } catch (e: Exception) {
        Log.d("TAG_debug", "${e.message}")
        emit(NetResult.Failure(e.message))
    }
}

suspend inline fun <reified T> Flow<NetResult<T>>.result(
    noinline start: (() -> Unit)?,
    noinline completion: (() -> Unit)?,
    crossinline success: (T) -> Unit
) {
    this@result.onStart { start?.invoke() }
        .onCompletion { completion?.invoke() }
        .collectLatest {
            withContext(Dispatchers.Main) {
                it.doSuccess {
                    success.invoke(it)
                }
                it.doFailure {
                    it?.showToast()
                }
            }
        }
}