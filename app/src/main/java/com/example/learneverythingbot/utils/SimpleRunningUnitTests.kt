package com.example.learneverythingbot.utils

import com.example.learneverythingbot.data.remote.ChatRemoteDataSource
import com.example.learneverythingbot.data.remote.retrofit.OpenAiService
import com.example.learneverythingbot.data.remote.retrofit.RetrofitClient
import kotlin.system.exitProcess

suspend fun main() {
    try {
        val service = RetrofitClient.retrofitInstance.create(OpenAiService::class.java)
        val remoteDataSource = ChatRemoteDataSource(service)
        val result = remoteDataSource.learnChatTopicGptResponse("Historia do Brasil")
        if (result.isSuccess) {
            val value = result.getOrNull()
            if (value != null) println(value) else println("Error: empty response from API")
        } else {
            println("Erro: ${result.exceptionOrNull()?.message}")
        }
    } finally {
        RetrofitClient.cancelAll()
        RetrofitClient.shutdown()
        exitProcess(0)
    }
}
