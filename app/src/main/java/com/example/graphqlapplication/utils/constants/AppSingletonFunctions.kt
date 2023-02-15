package com.example.graphqlapplication.utils.constants

import android.content.Context
import com.example.graphqlapplication.utils.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun Context.client() = withContext(Dispatchers.IO) {
    return@withContext RetrofitClient.apiInterface
}