package com.example.module_setting

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.showToast
import com.example.lib_base.view.BaseActivity
import com.example.lib_net.result
import com.example.lib_net.util.MmkvUtil
import com.example.module_setting.databinding.SettingActivityBinding
import com.example.module_setting.viewmodel.LoginOutViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

@Route(path = "/setting/activity")
class SettingActivity : BaseActivity() {

    private lateinit var settingBinding: SettingActivityBinding
    private val loginOutViewModel by viewModel<LoginOutViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        makeStatusBarIconDark()

        settingBinding = DataBindingUtil.setContentView(this, R.layout.setting_activity)

        initAction()
    }

    private fun initAction() {
        settingBinding.ivBack.setOnClickListener {
            onBackPressed()
        }
        settingBinding.btnLoginOut.setOnClickListener {
            if (MmkvUtil.isLogin()) {
                loginOutConfirm()
            } else {
                "暂未登录".showToast()
            }
        }
    }

    private fun loginOutConfirm() {
        AlertDialog.Builder(this).apply {
            setMessage("是否退出登录?")
            setPositiveButton("登出") { _, _ ->
                loginOut()
            }
            setNegativeButton("取消") { _, _ ->
                "操作取消".showToast()
            }
        }.show().apply {
            getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        }
    }

    private fun loginOut() = CoroutineScope(Dispatchers.IO).launch {
        loginOutViewModel.loginOut().result(null) {
            MmkvUtil.loginOut()
                    finish()
                    "login out success".showToast()
        }
    }
}