package com.example.lib_net

import okhttp3.Interceptor
import okhttp3.Response

/**
Created by chene on @date 20-12-6 下午4:39
 **/
class CookieInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val finalResponse: Response

        val cookies = MmkvUtil.getCookies()

        if (cookies.isNullOrEmpty()) {
            val originResponse = chain.proceed(chain.request())
//            if (!originResponse.headers("Set-Cookie").isNullOrEmpty()) {
//                val tempCookies = hashSetOf<String>()
//                originResponse.headers("Set-Cookie").forEach {
//                    tempCookies.add(it)
//                }
//                MmkvUtil.saveCookie(tempCookies)
//            }
            finalResponse = originResponse

        } else {
            val builder = chain.request().newBuilder()
            cookies.forEach {
                builder.addHeader("Cookie", it)
            }
            finalResponse = chain.proceed(builder.build())
        }

        return finalResponse
    }
}