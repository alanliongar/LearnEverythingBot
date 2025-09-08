package com.example.learneverythingbot.utils

import com.example.learneverythingbot.data.remote.ChatRemoteDataSource
import com.example.learneverythingbot.data.remote.retrofit.OpenAiService
import com.example.learneverythingbot.data.remote.retrofit.RetrofitClient
import com.example.learneverythingbot.domain.model.TopicItem
import kotlin.system.exitProcess

suspend fun main() {
    try {
        val service = RetrofitClient.retrofitInstance.create(OpenAiService::class.java)
        val remoteDataSource = ChatRemoteDataSource(service)
        val result = remoteDataSource.learnChatTopicGptResponse("Kotlin")
        if (result.isSuccess) {
            val value = result.getOrNull()
            if (value != null) {
                println(value)
                parsearTopicos(value)
            } else {
                println("Error: empty response from API")
            }
        } else {
            println("Erro: ${result.exceptionOrNull()?.message}")
        }
    } finally {
        RetrofitClient.cancelAll()
        RetrofitClient.shutdown()
        exitProcess(0)
    }
}

fun parsearTopicos(string: String) {
    val resultado = parseTopics(response = string)
    resultado.forEach { it ->
        println(it.toString())
    }
}

fun parseTopics(response: String): List<TopicItem> {
    return response.lines()
        .filter { it.isNotBlank() }
        .map { line ->
            val trimmed = line.trim()
            val level = when {
                Regex("^\\d+\\.\\d+").containsMatchIn(trimmed) -> 1
                Regex("^\\d+\\.").containsMatchIn(trimmed) -> 0
                else -> 0
            }
            val cleanedTitle = trimmed
                .replaceFirst(Regex("^\\d+(\\.\\d+)?\\s*"), "") // remove prefixo "1." ou "1.2"
                .replaceFirst(Regex("^\\.\\s*"), "") // remove "." isolado no início
                .trim()
            TopicItem(title = cleanedTitle, level = level)
        }
}
