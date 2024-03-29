package com.example.graphqlapplication.utils.network

import com.example.graphqlapplication.data.GetUsersResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST(EndPoint.API)
    suspend fun graphQlUser(@Body body: String): Response<String>
}