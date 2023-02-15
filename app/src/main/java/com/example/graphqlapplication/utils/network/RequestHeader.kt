package com.example.graphqlapplication.utils.network

import okhttp3.Interceptor
import okhttp3.Request

object RequestHeader {
    fun getHeaders(): Interceptor {
        return Interceptor { chain ->
            val request: Request?
            val original = chain.request()
            val requestBuilder = original.newBuilder().apply {
                addHeader("Accept-Encoding", "gzip, deflate, br")
                addHeader("Content-Type", "application/json")
            }
            request = requestBuilder.build()

//                com.okhatiapp.common.Logger.showLog("Header: ${SharedPrefsHelper.getAuthPrefs(a, SharedKeys.okhatiAccessToken, "")!!}")
            chain.proceed(request)
        }
    }
}