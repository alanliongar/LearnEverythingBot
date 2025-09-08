package com.example.learneverythingbot.data.local

import com.example.learneverythingbot.data.local.room.ChatHistoryDao
import com.example.learneverythingbot.data.local.room.ChatHistoryEntity
import com.example.learneverythingbot.di.DispatcherIO
import com.example.learneverythingbot.domain.model.ChatHistoryItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatLocalDataSource @Inject constructor(
    private val chatHistoryDao: ChatHistoryDao, @DispatcherIO val dispatcher: CoroutineDispatcher
) : LocalDataSource {

    override suspend fun getAllChatHistory(): Flow<List<ChatHistoryItem>> =
        chatHistoryDao.getAllChatHistory() // Flow<List<Entity>>
            .map { entities ->
                entities.map { it.toDomain() } // mapeamento pesado fica no IO
            }.flowOn(context = dispatcher) // 👈 aplica aqui UMA vez

    override suspend fun deleteAllChat() {
        chatHistoryDao.deleteAllChat()
    }

    override suspend fun deleteChat(id: Int) {
        chatHistoryDao.deleteChat(id = id)
    }

    private fun ChatHistoryEntity.toDomain() = ChatHistoryItem(
        id = id, userMessage = userMessage, aiResponse = aiResponse, timestamp = timestamp
    )

    override suspend fun insertChatHistory(chatHistoryItem: ChatHistoryItem) {
        val chatHistoryEntityItem = ChatHistoryEntity(
            id = chatHistoryItem.id,
            userMessage = chatHistoryItem.userMessage,
            aiResponse = chatHistoryItem.aiResponse,
            timestamp = chatHistoryItem.timestamp
        )
        chatHistoryDao.insertChatHistory(chatHistoryEntityItem)
    }

    override suspend fun insertTopicHistory(chatHistoryItem: ChatHistoryItem) {
        chatHistoryDao.insertChatHistory(
            ChatHistoryEntity(
                id = chatHistoryItem.id,
                userMessage = chatHistoryItem.userMessage,
                aiResponse = chatHistoryItem.aiResponse,
                timestamp = chatHistoryItem.timestamp
            )
        )
    }

    override suspend fun deleteAllTopic() {
        chatHistoryDao.deleteAllChat()
    }

    override suspend fun deleteTopic(id: Int) {
        chatHistoryDao.deleteChat(id = id)
    }

    override suspend fun getChatById(id: Int): ChatHistoryItem? {
        return chatHistoryDao.getChatById(id)?.toDomain()
    }
}