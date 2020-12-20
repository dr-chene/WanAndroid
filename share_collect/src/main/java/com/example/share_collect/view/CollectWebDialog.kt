package com.example.share_collect.view

import android.content.Context
import android.view.View
import com.example.lib_base.showToast
import com.example.lib_net.request
import com.example.lib_net.result
import com.example.share_collect.MyBaseAlertDialog
import com.example.share_collect.R
import com.example.share_collect.bean.CollectWeb
import com.example.share_collect.databinding.AlertInputBinding
import com.example.share_collect.remote.ModifyCollectWebService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

/**
 *Created by chene on 20-12-19
 */
class CollectWebDialog(
    context: Context,
    private val web: CollectWeb? = null
) : MyBaseAlertDialog(context) {
    private val modifyApi by inject(ModifyCollectWebService::class.java)

    override fun initView(binding: AlertInputBinding) {
        binding.apply {
            alertTitle.text = context.resources.getText(R.string.item_web)
            alertInputLayoutTitle.hint = "name"
            alertConfirmBtn.setText(R.string.alert_confirm_btn_collect)
            web?.let { w ->
                alertDeleteBtn.visibility = View.VISIBLE
                alertDeleteBtn.setOnClickListener {
                    delete(w.id)
                }
                alertInputTitle.setText(w.name)
                alertInputLink.setText(w.link)
            }
        }
    }

    override fun share(title: String, link: String, author: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (web == null) {
                repository.collectWeb(title, link).result(null, null) {
                    dismiss()
                    "网站收藏成功".showToast()
                }
            } else {
                modifyApi.modify(web.id, title, link).request().result(null, null) {
                    dismiss()
                    "网站修改成功".showToast()
                }
            }
        }
    }

    private fun delete(id: Int) = CoroutineScope(Dispatchers.IO).launch{
        modifyApi.delete(id).request().result(null, null) {
            dismiss()
            "网站删除成功".showToast()
        }
    }
}