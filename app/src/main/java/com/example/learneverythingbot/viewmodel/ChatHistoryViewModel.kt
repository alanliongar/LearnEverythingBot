package com.example.learneverythingbot.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.learneverythingbot.data.local.database.AppDatabase
import com.example.learneverythingbot.data.repository.ChatRepository
import com.example.learneverythingbot.domain.model.ChatHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChatHistoryViewModel(
    application: Application,
    var repository: ChatRepository
) :
    AndroidViewModel(application) {

    private val _chatHistory = MutableStateFlow<List<ChatHistory>>(emptyList())
    val chatHistory: MutableStateFlow<List<ChatHistory>> = _chatHistory

    init {
        val database= AppDatabase.getInstance(application)
        repository = ChatRepository(database.chatHistoryDao())

        viewModelScope.launch {
            repository.allchats.collect { chats ->
                _chatHistory.value = chats
            }
        }

    }


    fun saveChat(userMessage: String, aiResponse: String) {
        viewModelScope.launch {
            val chatHistory = ChatHistory(userMessage = userMessage, aiResponse = aiResponse)
            repository.insertChat(chatHistory)

        }
    }

    fun deleteChat(id: Int){
        viewModelScope.launch {
            repository.deleteChat(id)
        }
    }

    fun deleteAllChat(){
        viewModelScope.launch {
            repository.deleteAllChat()

        }
    }

}