package com.example.learneverythingbot.data.local

import com.example.learneverythingbot.data.local.room.QuizEntity
import com.example.learneverythingbot.domain.model.ChatHistoryItem
import com.example.learneverythingbot.domain.model.QuizQuestion
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertChatHistory(chatHistoryItem: ChatHistoryItem)

    suspend fun insertTopicHistory(chatHistoryItem: ChatHistoryItem)

    suspend fun deleteAllTopic()

    suspend fun deleteTopic(topic: String)

    suspend fun getAllChatHistory(): Flow<List<ChatHistoryItem>>

    suspend fun deleteAllChat()

    suspend fun deleteChat(topic: String)

    suspend fun getChatByTopic(topic: String): ChatHistoryItem?

    suspend fun getSummaryByTopicAndSubTopic(topic: String, subTopic: String): String?

    suspend fun insertSummary(topic: String, subTopic: String, secondAiResponse: String)

    suspend fun insertQuizQuestions(topic: String, subTopic: String, questions: List<QuizQuestion>)

    suspend fun getQuizQuestionsByTopic(topic: String): List<QuizQuestion>?

    suspend fun getQuizQuestionsByTopicAndSubTopic(topic: String, subTopic: String): List<QuizQuestion>?

    suspend fun countByTopic(topic: String): Int
}