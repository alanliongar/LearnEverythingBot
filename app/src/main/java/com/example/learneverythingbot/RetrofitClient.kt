package com.example.learneverythingbot

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val OPENAI_BASE_URL = "https://api.openai.com/v1/"

    private val okHttp: OkHttpClient by lazy {
        val apiKey = BuildConfig.API_KEY
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original: Request = chain.request()
                val request: Request = original.newBuilder()
                    .header("Authorization", "Bearer $apiKey")
                    .header("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(OPENAI_BASE_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun cancelAll() {
        okHttp.dispatcher().cancelAll()
    }

    fun shutdown() {
        okHttp.dispatcher().executorService().shutdown()
        okHttp.connectionPool().evictAll()
        okHttp.cache()?.close()
    }
}
