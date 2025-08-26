package com.example.learneverythingbot.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.learneverythingbot.data.local.database.AppDatabase
import com.example.learneverythingbot.data.repository.ChatRepository
import com.example.learneverythingbot.domain.model.ChatHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChatHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ChatRepository
    val chatHistory = MutableStateFlow<List<ChatHistory>>(emptyList())
    val drawerVisible = MutableStateFlow(false)

    init {
        val database = AppDatabase.getInstance(application)
        val dao = database.chatHistoryDao()
        repository = ChatRepository(dao)
        loadChatHistory()
    }

    private fun loadChatHistory() {
        viewModelScope.launch {
            repository.allchats.collect { chats ->
                chatHistory.value = chats
            }
        }
    }

    fun toggleDrawer() {
        drawerVisible.value = !drawerVisible.value
    }

    fun showDrawer() {
        drawerVisible.value = true
    }

    fun hideDrawer() {
        drawerVisible.value = false
    }

    fun deleteChat(id: Int) {
        viewModelScope.launch {
            repository.deleteChat(id)
        }
    }

    fun deleteAllChat() {
        viewModelScope.launch {
            repository.deleteAllChat()
        }
    }

    fun saveChat(userMessage: String, aiResponse: String) {
        viewModelScope.launch {
            val chatHistory = ChatHistory(
                userMessage = userMessage,
                aiResponse = aiResponse,
                timestamp = System.currentTimeMillis()
            )
            repository.insertChat(chatHistory)
        }
    }
}