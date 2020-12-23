package com.example.share_collect.view

import android.content.Context
import android.view.View
import com.example.lib_base.showToast
import com.example.lib_net.result
import com.example.share_collect.MyBaseAlertDialog
import com.example.share_collect.R
import com.example.share_collect.databinding.AlertInputBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *Created by chene on 20-12-19
 */
class CollectArticleDialog(
    context: Context
): MyBaseAlertDialog(context) {

    override fun initView(binding: AlertInputBinding) {
        binding.apply {
            alertTitle.text = context.resources.getText(R.string.item_collect)
            alertInputLayoutAuthor.visibility = View.VISIBLE
            alertConfirmBtn.setText(R.string.alert_confirm_btn_collect)
        }
    }

    override fun share(title: String, link: String, author: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.collectArticle(title, author, link).result(null) {
                dismiss()
                "文章收藏成功".showToast()
            }
        }
    }
}