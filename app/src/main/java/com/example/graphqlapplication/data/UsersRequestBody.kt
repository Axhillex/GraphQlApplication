package com.example.graphqlapplication.data

import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class UsersRequestBody(
    val requiredData: String? = null,
) {
    internal suspend fun createGetQuery() = withContext(Dispatchers.IO) {
        return@withContext JsonObject().apply {
            addProperty("query", "query { users { data { $requiredData } } }")
        }.toString()
    }
    internal suspend fun createRemoveQuery() = withContext(Dispatchers.IO) {
        return@withContext JsonObject().apply {
            addProperty("query", "mutation { deleteUser ( id: $requiredData) }")
        }.toString()
    }
}