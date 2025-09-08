package com.example.learneverythingbot.data

import com.example.learneverythingbot.data.local.LocalDataSource
import com.example.learneverythingbot.data.remote.RemoteDataSource
import com.example.learneverythingbot.domain.model.ChatHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource
) {
    suspend fun getGptResponse(topic: String): Result<ChatHistory> {
        return try {
            val result = remote.learnChatTopicGptResponse(topic = topic)
            if (result.isSuccess) {
                val remoteGptResponse = result.getOrNull() ?: ""
                if (remoteGptResponse.isNotEmpty()) {
                    val chatHistory = ChatHistory(
                        id = 0, // ID 0 para novo chat (Room irá auto-generate)
                        userMessage = topic,
                        aiResponse = remoteGptResponse
                    )
                    Result.success(chatHistory)
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



    suspend fun getSubTopicSummary(topic: String, subTopic: String): Result<String> {
        return try{
            remote.getSubTopicSummary(topic, subTopic)
        }catch (e: Exception){
            Result.failure(Exception(e.message ?: "Erro ao buscar resumo do subtopico"))
        }
    }

    suspend fun insertChat(chatHistory: ChatHistory) {
        local.insertChatHistory(chatHistory = chatHistory)
    }

    suspend fun deleteChat(id: Int) {
        local.deleteChat(id = id)
    }

    suspend fun deleteAllChat() {
        local.deleteAllChat()
    }

    fun getAllChatHistory(): Flow<List<ChatHistory>> {
        return local.getAllChatHistory()
    }

    suspend fun getChatById(id: Int): ChatHistory? {
        return local.getChatById(id)
    }
}