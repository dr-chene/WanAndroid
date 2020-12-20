package com.example.module_login

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.lib_base.save
import com.example.lib_base.showToast
import com.example.lib_base.view.BaseActivity
import com.example.lib_net.bean.doFailure
import com.example.lib_net.bean.doSuccess
import com.example.module_login.databinding.LoginActivityBinding
import com.example.module_login.repository.LoginRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

@Route(path = "/login/activity")
class LoginActivity : BaseActivity() {

    private lateinit var loginBinding: LoginActivityBinding
    private val loginRepository by inject<LoginRepository>()
    private var login = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        makeStatusBarIconDark()
        loginBinding = DataBindingUtil.setContentView(this, R.layout.login_activity)

        initView()
        initAction()
    }

    private fun initView() {
        loginBinding.input.loginInputRepassword.visibility = View.GONE
    }

    private fun initAction() {
        loginBinding.ivBack.setOnClickListener {
            onBackPressed()
        }
        loginBinding.btnRegister.setOnClickListener {
            synchronized(login) {
                loginBinding.apply {
                    if (login) {
                        btnLogin.text = resources.getText(R.string.register_btn)
                        btnRegister.text = resources.getText(R.string.login_btn)
                    } else {
                        btnLogin.text = resources.getText(R.string.login_btn)
                        btnRegister.text = resources.getText(R.string.register_btn)
                    }
                }
                login = !login
                loginBinding.input.loginInputRepassword.visibility =
                    if (!login) View.VISIBLE else View.GONE
            }
        }
        loginBinding.btnLogin.setOnClickListener {
            if (login) {
                loginBinding.input.apply {
                    login(
                        loginAccount.text.toString(),
                        loginPassword.text.toString()
                    )
                }
            } else {
                loginBinding.input.apply {
                    register(
                        loginAccount.text.toString(),
                        loginPassword.text.toString(),
                        loginRepassword.text.toString()
                    )
                }
            }
        }
    }

    private fun login(username: String, password: String) =
        CoroutineScope(Dispatchers.IO).launch {
            loginRepository.login(username, password).collectLatest {
                withContext(Dispatchers.Main) {
                    it.doSuccess { user ->
                        user.save()
                        loginSuccess()
                        "login success".showToast()
                    }
                    it.doFailure { t ->
                        t?.showToast()
                    }
                }
            }
        }

    private fun register(username: String, password: String, repassword: String) =
        CoroutineScope(Dispatchers.IO).launch {
            loginRepository.register(username, password, repassword).collectLatest {
                withContext(Dispatchers.Main) {
                    it.doSuccess { user ->
                        user.save()
                        loginSuccess()
                        "register success".showToast()
                    }
                    it.doFailure { t ->
                        t?.showToast()
                    }
                }
            }
        }

    private fun loginSuccess() {
        finish()
    }
}