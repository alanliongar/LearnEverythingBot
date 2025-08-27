package com.example.learneverythingbot.data.remote

interface RemoteDataSource {
    suspend fun learningTopicsResponse(topic: String): Result<String>
}