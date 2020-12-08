package com.example.lib_base

import android.content.Context
import android.widget.Toast
import org.koin.java.KoinJavaComponent.get

/**
Created by chene on @date 20-12-6 上午11:40
 **/

fun String.showToast() {
    Toast.makeText(get(Context::class.java), this, Toast.LENGTH_SHORT).show()
}