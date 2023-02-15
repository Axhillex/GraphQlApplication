package com.example.graphqlapplication.utils.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    val apiInterface: ApiService by lazy {
        val levelType: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY

        val logging = HttpLoggingInterceptor()
        logging.level = levelType

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
            .writeTimeout(5, TimeUnit.MINUTES) // write timeout
            .readTimeout(5, TimeUnit.MINUTES) // read timeout
            .addInterceptor(logging)
            .addNetworkInterceptor(RequestHeader.getHeaders())

        val retrofitClient = Retrofit.Builder()
            .baseUrl(EndPoint.BASE_URL)
            .client(okHttpClient.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

        retrofitClient.build().create(ApiService::class.java)
    }
}