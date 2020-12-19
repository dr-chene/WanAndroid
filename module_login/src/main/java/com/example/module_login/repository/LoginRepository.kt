package com.example.module_login.repository

import android.util.Log
import com.example.lib_base.bean.User
import com.example.lib_net.bean.NetBean
import com.example.lib_net.bean.NetResult
import com.example.lib_net.util.MmkvUtil
import com.example.module_login.remote.LoginService
import kotlinx.coroutines.flow.flow
import org.koin.java.KoinJavaComponent.inject
import retrofit2.Response

/**
Created by chene on @date 20-12-10 下午10:24
 **/
class LoginRepository {

    private val loginApi by inject(LoginService::class.java)

    suspend fun login(username: String, password: String) = userOpera(
        loginApi.login(username, password)
    )

    suspend fun register(username: String, password: String, repassword: String) = userOpera(
        loginApi.register(username, password, repassword)
    )

    private fun <T> cacheCookie(response: Response<T>){
        val cookieSet = HashSet<String>()
        response.headers().values("Set-Cookie").forEach {
            cookieSet.add(it)
        }
        MmkvUtil.saveCookie(cookieSet)
    }

    private fun userOpera(user: Response<NetBean<User>>) =  flow {
        try {
            user.let {
                if (it.body()?.data == null) {
                    emit(NetResult.Failure(it.body()?.errorMsg))
                } else {
                    cacheCookie(it)
                    it.body()?.data?.let { user -> emit(NetResult.Success(user)) }
                }
            }
        } catch (e: Exception) {
            Log.d("TAG_debug_login", "login: ${e.message}")
            emit(NetResult.Failure(e.message))
        }
    }
}