package com.example.share_collect.view

import android.content.Context
import com.example.lib_base.showToast
import com.example.lib_net.result
import com.example.share_collect.MyBaseAlertDialog
import com.example.share_collect.databinding.AlertInputBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *Created by chene on 20-12-19
 */
class ShareArticleDialog(
    context: Context
) : MyBaseAlertDialog(context) {
    override fun initView(binding: AlertInputBinding) {

    }

    override fun share(title: String, link: String, author: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.shareArticle(title, link).result(null) {
                dismiss()
                "文章分享成功".showToast()
            }
        }
    }
}