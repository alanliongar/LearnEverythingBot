package com.example.learneverythingbot

suspend fun main() {
    try {
        val service = RetrofitClient.retrofitInstance.create(OpenAiService::class.java)
        val remoteDataSource = LearningTopicsRemoteDataSource(service)
        val result = remoteDataSource.learningTopicsResponse("Historia do Brasil")
        if (result.isSuccess) {
            val value = result.getOrNull()
            if (value != null) println(value) else println("Error: empty response from API")
        } else {
            println("Erro: ${result.exceptionOrNull()?.message}")
        }
    } finally {
        RetrofitClient.cancelAll()
        RetrofitClient.shutdown()
        kotlin.system.exitProcess(0)
    }
}
