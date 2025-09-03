package com.example.learneverythingbot.data.local

import com.example.learneverythingbot.data.local.room.ChatHistoryDao
import com.example.learneverythingbot.data.local.room.ChatHistoryEntity
import com.example.learneverythingbot.di.DispatcherIO
import com.example.learneverythingbot.domain.model.HistoryItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatLocalDataSource @Inject constructor(
    private val chatHistoryDao: ChatHistoryDao,
    @DispatcherIO val dispatcher: CoroutineDispatcher
) : LocalDataSource {

    override fun getAllChatHistory(): Flow<List<HistoryItem>> =
        chatHistoryDao.getAllChatHistory() // Flow<List<Entity>>
            .map { entities ->
                entities.map { it.toDomain() } // mapeamento pesado fica no IO
            }
            .flowOn(context = dispatcher) // 👈 aplica aqui UMA vez

    private fun ChatHistoryEntity.toDomain() = HistoryItem(
        id = id,
        userMessage = userMessage,
        aiResponse = aiResponse,
        timestamp = timestamp
    )

    override suspend fun insertTopicHistory(historyItem: HistoryItem) {
        chatHistoryDao.insertChatHistory(
            ChatHistoryEntity(
                id = historyItem.id,
                userMessage = historyItem.userMessage,
                aiResponse = historyItem.aiResponse,
                timestamp = historyItem.timestamp
            )
        )
    }

    override suspend fun deleteAllTopic() {
        chatHistoryDao.deleteAllChat()
    }

    override suspend fun deleteTopic(id: Int) {
        chatHistoryDao.deleteChat(id = id)
    }
}