package com.example.learneverythingbot.data.repository

import com.example.learneverythingbot.data.local.dao.ChatHistoryDao
import com.example.learneverythingbot.domain.model.ChatHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatRepository(private val chatHistoryDao: ChatHistoryDao) {


    val allchats: Flow<List<ChatHistory>> = chatHistoryDao.getAllChatHistory().map { entities ->

        entities.map { entity -> ChatHistory.fromEntity(entity) }

    }

    suspend fun insertChat(chatHistory: ChatHistory){
        chatHistoryDao.insertChatHistory(ChatHistory.toEntity(chatHistory))
    }
    suspend fun deleteChat(id: Int){
        chatHistoryDao.deleteChat(id)
    }
    suspend fun deleteAllChat(){
        chatHistoryDao.deleteAllChat()
    }



}