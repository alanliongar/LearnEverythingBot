package com.example.learneverythingbot

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiService {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun getLearningTopics(@Body request: OpenAiRequest): Response<OpenAiResponse>
}