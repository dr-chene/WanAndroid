package com.example.module_setting

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.showToast
import com.example.lib_base.view.BaseActivity
import com.example.lib_net.MmkvUtil
import com.example.lib_net.doFailure
import com.example.lib_net.doSuccess
import com.example.module_setting.databinding.SettingActivityBinding
import com.example.module_setting.repository.LoginOutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

@Route(path = "/setting/activity")
class SettingActivity : BaseActivity() {

    private lateinit var settingBinding: SettingActivityBinding
    private val loginOutRepository by inject<LoginOutRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        makeStatusBarIconDark()

        settingBinding = DataBindingUtil.setContentView(this, R.layout.setting_activity)

        initView()
        initAction()
        subscribe()
    }

    private fun initView() {

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

    private fun subscribe() {

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
        loginOutRepository.loginOut().collectLatest {
            withContext(Dispatchers.Main) {
                it.doSuccess {
                    MmkvUtil.loginOut()
                    finish()
                    "login out success".showToast()
                }
                it.doFailure {
                    it?.showToast()
                }
            }
        }
    }
}