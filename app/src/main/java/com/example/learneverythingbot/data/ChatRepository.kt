package com.example.learneverythingbot.data

import com.example.learneverythingbot.data.local.LocalDataSource
import com.example.learneverythingbot.data.remote.RemoteDataSource
import com.example.learneverythingbot.domain.model.HistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource
) {
    suspend fun getGptResponse(topic: String): Result<HistoryItem> {
        var endResult: Result<HistoryItem>
        try {
            val result = remote.learnChatTopicGptResponse(topic = topic)
            if (result.isSuccess) {
                val remoteGptResponse = result.getOrNull() ?: ""
                if (remoteGptResponse.isNotEmpty()) {
                    val lastResponse = HistoryItem(
                        id = 0,
                        userMessage = topic,
                        aiResponse = remoteGptResponse
                    )
                    endResult = Result.success(lastResponse)
                } else {
                    endResult = Result.failure<HistoryItem>(Exception("Algo deu errado!"))
                }
            } else {
                endResult = Result.failure<HistoryItem>(Exception("Algo deu errado!"))
            }
        } catch (ex: Exception) {
            endResult = Result.failure<HistoryItem>(Exception(ex.message ?: "Algo deu errado!"))
        }
        return endResult
    }

    suspend fun insertTopic(historyItem: HistoryItem) {
        local.insertTopicHistory(historyItem = historyItem)
    }

    suspend fun deleteTopic(id: Int){
        local.deleteTopic(id = id)
    }

    suspend fun deleteAllTopic(){
        local.deleteAllTopic()
    }

    fun getAllTopicHistory(): Flow<List<HistoryItem>> {
        val placeholder = HistoryItem(0, "", "", System.currentTimeMillis())
        return local.getAllChatHistory() //já vem do IO
            .map { list -> if (list.isEmpty()) listOf(placeholder) else list }
    }
}