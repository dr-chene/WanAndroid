package com.example.lib_net

/**
Created by chene on @date 20-12-9 上午8:55
 **/
sealed class NetResult<out T> {
    data class Success<out T>(val value: T) : NetResult<T>() {

        fun result() = value
    }

    data class Failure(val throwable: Throwable?) : NetResult<Nothing>()
}

inline fun <reified T> NetResult<T>.doSuccess(success: (T) -> Unit) {
    if (this is NetResult.Success) {
        success(value)
    }
}

inline fun <reified T> NetResult<T>.doFailure(failure: (Throwable?) -> Unit) {
    if (this is NetResult.Failure) {
        failure(throwable)
    }
}