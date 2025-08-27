package com.example.learneverythingbot.data.local

import com.example.learneverythingbot.data.local.dao.ChatHistoryDao
import com.example.learneverythingbot.data.local.entity.ChatHistoryEntity
import com.example.learneverythingbot.domain.model.ChatHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ChatLocalDataSource(
    private val chatHistoryDao: ChatHistoryDao
) : LocalDataSource {

    override fun getAllChatHistory(): Flow<List<ChatHistory>> =
        chatHistoryDao.getAllChatHistory() // Flow<List<Entity>>
            .map { entities ->
                entities.map { it.toDomain() } // mapeamento pesado fica no IO
            }
            .flowOn(Dispatchers.IO) // 👈 aplica aqui UMA vez

    private fun ChatHistoryEntity.toDomain() = ChatHistory(
        id = id,
        userMessage = userMessage,
        aiResponse = aiResponse,
        timestamp = timestamp
    )

    override suspend fun insertChatHistory(chatHistory: ChatHistory) {
        chatHistoryDao.insertChatHistory(
            ChatHistoryEntity(
                id = chatHistory.id,
                userMessage = chatHistory.userMessage,
                aiResponse = chatHistory.aiResponse,
                timestamp = chatHistory.timestamp
            )
        )
    }

    override suspend fun deleteAllChat() {
        chatHistoryDao.deleteAllChat()
    }

    override suspend fun deleteChat(id: Int) {
        chatHistoryDao.deleteChat(id = id)
    }
}