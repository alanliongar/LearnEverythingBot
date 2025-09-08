package com.example.learneverythingbot.data

import com.example.learneverythingbot.data.local.LocalDataSource
import com.example.learneverythingbot.data.remote.RemoteDataSource
import com.example.learneverythingbot.domain.model.ChatHistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource
) {
    suspend fun getGptResponse(topic: String): Result<ChatHistoryItem> {
        return try {
            val result = remote.learnChatTopicGptResponse(topic = topic)
            if (result.isSuccess) {
                val remoteGptResponse = result.getOrNull() ?: ""
                if (remoteGptResponse.isNotEmpty()) {
                    val chatHistoryItem = ChatHistoryItem(
                        id = 0, // ID 0 para novo chat (Room irá auto-generate)
                        userMessage = topic,
                        aiResponse = remoteGptResponse
                    )
                    Result.success(chatHistoryItem)
                } else {
                    Result.failure(Exception("Resposta vazia da API"))
                }
            } else {
                Result.failure(Exception("Falha na chamada da API"))
            }
        } catch (ex: Exception) {
            Result.failure(Exception(ex.message ?: "Erro desconhecido"))
        }
    }

    suspend fun getAllTopicHistory(): Flow<List<ChatHistoryItem>> {
        val placeholder = ChatHistoryItem(0, "", "", System.currentTimeMillis())
        return local.getAllChatHistory() //já vem do IO
            .map { list -> if (list.isEmpty()) listOf(placeholder) else list }
    }

    suspend fun deleteAllTopic(){
        local.deleteAllTopic()
    }

    suspend fun insertTopic(chatHistoryItem: ChatHistoryItem) {
        local.insertTopicHistory(chatHistoryItem = chatHistoryItem)
    }


    suspend fun getSubTopicSummary(topic: String, subTopic: String): Result<String> {
        return try {
            remote.getSubTopicSummary(topic, subTopic)
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Erro ao buscar resumo do subtopico"))
        }
    }

    suspend fun insertChat(chatHistoryItem: ChatHistoryItem) {
        local.insertChatHistory(chatHistoryItem = chatHistoryItem)
    }

    suspend fun deleteChat(id: Int) {
        local.deleteChat(id = id)
    }

    suspend fun deleteAllChat() {
        local.deleteAllChat()
    }

    suspend fun getAllChatHistory(): Flow<List<ChatHistoryItem>> {
        return local.getAllChatHistory()
    }

    suspend fun getChatById(id: Int): ChatHistoryItem? {
        return local.getChatById(id)
    }
}