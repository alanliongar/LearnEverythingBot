package com.example.learneverythingbot.data.remote

interface RemoteDataSource {
    suspend fun learnChatTopicGptResponse(topic: String): Result<String>
}