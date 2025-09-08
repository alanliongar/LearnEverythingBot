package com.example.learneverythingbot.data.local

import com.example.learneverythingbot.data.local.room.ChatHistoryDao
import com.example.learneverythingbot.data.local.room.ChatHistoryEntity
import com.example.learneverythingbot.data.local.room.TopicSummaryDao
import com.example.learneverythingbot.data.local.room.TopicSummaryEntity
import com.example.learneverythingbot.di.DispatcherIO
import com.example.learneverythingbot.domain.model.ChatHistoryItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatLocalDataSource @Inject constructor(
    private val chatSummaryDao: TopicSummaryDao,
    private val chatHistoryDao: ChatHistoryDao,
    @DispatcherIO val dispatcher: CoroutineDispatcher
) : LocalDataSource {

    override suspend fun getSummaryByTopicAndSubTopic(
        topic: String,
        subTopic: String
    ): String? {
        val response = chatSummaryDao.getSummary(topic = topic, subtopicTitle = subTopic)
        if (response == null) {
            return null
        } else {
            return response.secondAiResponse
        }
    }

    override suspend fun insertSummary(topic: String, subTopic: String, secondAiResponse: String) {
        val topicSummaryEntity = TopicSummaryEntity(
            chatHistoryUserMessage = topic,
            subtopicTitle = subTopic,
            secondAiResponse = secondAiResponse,
        )
        chatSummaryDao.insert(topicSummaryEntity)
    }

    override suspend fun getAllChatHistory(): Flow<List<ChatHistoryItem>> =
        chatHistoryDao.getAllChatHistory() // Flow<List<Entity>>
            .map { entities ->
                entities.map { it.toDomain() } // mapeamento pesado fica no IO
            }.flowOn(context = dispatcher) // 👈 aplica aqui UMA vez

    override suspend fun deleteAllChat() {
        chatHistoryDao.deleteAllChat()
    }

    override suspend fun deleteChat(topic: String) {
        chatHistoryDao.deleteChat(userMessage = topic)
    }

    private fun ChatHistoryEntity.toDomain() = ChatHistoryItem(
        userMessage = userMessage, aiResponse = aiResponse, timestamp = timestamp
    )

    override suspend fun insertChatHistory(chatHistoryItem: ChatHistoryItem) {
        val chatHistoryEntityItem = ChatHistoryEntity(
            userMessage = chatHistoryItem.userMessage,
            aiResponse = chatHistoryItem.aiResponse,
            timestamp = chatHistoryItem.timestamp
        )
        chatHistoryDao.insertChatHistory(chatHistoryEntityItem)
    }

    override suspend fun insertTopicHistory(chatHistoryItem: ChatHistoryItem) {
        chatHistoryDao.insertChatHistory(
            ChatHistoryEntity(
                userMessage = chatHistoryItem.userMessage,
                aiResponse = chatHistoryItem.aiResponse,
                timestamp = chatHistoryItem.timestamp
            )
        )
    }

    override suspend fun deleteAllTopic() {
        chatHistoryDao.deleteAllChat()
    }

    override suspend fun deleteTopic(userMessage: String) {
        chatHistoryDao.deleteChat(userMessage = userMessage)
    }

    override suspend fun getChatByTopic(topic: String): ChatHistoryItem? {
        return chatHistoryDao.getChatByTopic(topic = topic)?.toDomain()
    }
}