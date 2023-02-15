package com.example.graphqlapplication.data

import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class GetUsersRequestBody(
    val requiredData: String? = null,
) {
    internal suspend fun createQuery() = withContext(Dispatchers.IO) {
        return@withContext JsonObject().apply {
            addProperty("query", "query { users { data { $requiredData } } }")
        }.toString()
    }
}