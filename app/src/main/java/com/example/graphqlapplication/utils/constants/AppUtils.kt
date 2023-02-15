package com.example.graphqlapplication.utils.constants

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AppUtils {

    internal suspend fun convertFromJson(data: String?, javaClass: Class<*>) = withContext(Dispatchers.IO) {
        return@withContext Gson().fromJson(data, javaClass)
    }
}