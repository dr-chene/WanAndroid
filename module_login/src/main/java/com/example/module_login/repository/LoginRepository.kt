package com.example.module_login.repository

import com.example.module_login.remote.LoginService

class LoginRepository(
    private val api: LoginService
) {
    suspend fun remoteLogin(username: String, password: String) =
        api.login(username, password)

    suspend fun remoteRegister(username: String, password: String, repassword: String) =
        api.register(username, password, repassword)
}