package com.example.learneverythingbot.data.remote

import com.example.learneverythingbot.data.model.OpenAiRequest
import com.example.learneverythingbot.data.model.OpenAiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiService {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun getLearningTopics(@Body request: OpenAiRequest): Response<OpenAiResponse>
}