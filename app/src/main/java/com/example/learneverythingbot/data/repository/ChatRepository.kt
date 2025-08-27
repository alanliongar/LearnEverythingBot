package com.example.learneverythingbot.data.repository

import com.example.learneverythingbot.data.local.LocalDataSource
import com.example.learneverythingbot.data.remote.RemoteDataSource
import com.example.learneverythingbot.domain.model.ChatHistory
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

class ChatRepository(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource
) {


    suspend fun getGptResponse(topic: String): Result<ChatHistory> {
        var endResult: Result<ChatHistory>
        try {
            val result = remote.learningTopicsResponse(topic = topic)
            if (result.isSuccess) {
                val remoteGptResponse = result.getOrNull() ?: ""
                if (remoteGptResponse.isNotEmpty()) {
                    val lastResponse = ChatHistory(
                        id = 0,
                        userMessage = topic,
                        aiResponse = remoteGptResponse
                    )
                    local.insertChatHistory(
                        chatHistory = lastResponse
                    )
                    endResult = Result.success(lastResponse)
                } else {
                    endResult = Result.failure<ChatHistory>(Exception("Algo deu errado!"))
                }
            } else {
                endResult = Result.failure<ChatHistory>(Exception("Algo deu errado!"))
            }
        } catch (ex: Exception) {
            endResult = Result.failure<ChatHistory>(Exception(ex.message ?: "Algo deu errado!"))
        }
        return endResult
    }

    suspend fun insertChat(chatHistory: ChatHistory) {
        local.insertChatHistory(chatHistory = chatHistory)
    }

    suspend fun deleteChat(id: Int){
        local.deleteChat(id = id)
    }

    suspend fun deleteAllChat(){
        local.deleteAllChat()
    }

    fun getAllChatHistory(): Flow<List<ChatHistory>> {
        val placeholder = ChatHistory(0, "", "", System.currentTimeMillis())
        return local.getAllChatHistory() //já vem do IO
            .map { list -> if (list.isEmpty()) listOf(placeholder) else list }
    }
}