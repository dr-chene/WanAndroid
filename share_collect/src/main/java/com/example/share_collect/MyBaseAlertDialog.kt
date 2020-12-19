package com.example.share_collect

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.example.lib_base.showToast
import com.example.share_collect.databinding.AlertInputBinding
import com.example.share_collect.repository.ShareCollectRepository
import org.koin.java.KoinJavaComponent.inject

/**
 *Created by chene on 20-12-19
 */
abstract class MyBaseAlertDialog(
    context: Context
) : AlertDialog(context) {
    private lateinit var binding: AlertInputBinding
    protected val repository by inject(ShareCollectRepository::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AlertInputBinding.inflate(
            LayoutInflater.from(context)
        )
        window?.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        setContentView(binding.root)

        initView(binding)
        initAction()
    }

    private fun initAction() {
        binding.apply {
            alertConfirmBtn.setOnClickListener {
                val title = alertInputTitle.text.toString()
                val link = alertInputLink.text.toString()
                val author = alertInputAuthor.text.toString()
                if (title.isEmpty() || link.isEmpty()) {
                    "请检查输入是否为空".showToast()
                } else share(title, link, author)
            }
        }
        binding.alertClose.setOnClickListener {
            this.dismiss()
        }
    }

    abstract fun initView(binding: AlertInputBinding)

    abstract fun share(title: String, link: String, author: String)
}