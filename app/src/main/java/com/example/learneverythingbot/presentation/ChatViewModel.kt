package com.example.learneverythingbot.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learneverythingbot.data.ChatRepository
import com.example.learneverythingbot.di.DispatcherIO
import com.example.learneverythingbot.domain.model.Chat
import com.example.learneverythingbot.domain.model.ChatHistory
import com.example.learneverythingbot.domain.model.ChatHistoryDrawerUiState
import com.example.learneverythingbot.domain.model.ChatScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository,
    @DispatcherIO val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _drawerVisible = MutableStateFlow(false)
    val drawerVisible: StateFlow<Boolean> = _drawerVisible

    private val _chatScreenUiState = MutableStateFlow(ChatScreenUiState())
    val chatScreenUiState: StateFlow<ChatScreenUiState> = _chatScreenUiState

    private val _chatHistoryDrawerUiState =
        MutableStateFlow(ChatHistoryDrawerUiState())
    val chatHistoryDrawerUiState: StateFlow<ChatHistoryDrawerUiState> = _chatHistoryDrawerUiState

    init {
        viewModelScope.launch(context = dispatcher) {
            _chatHistoryDrawerUiState.value = ChatHistoryDrawerUiState(isLoading = true)
            repository.getAllChatHistory().collect { history ->
                _chatHistoryDrawerUiState.value = ChatHistoryDrawerUiState(
                    chatHistory = history
                )
            }
        }
    }

    fun toggleDrawer() {
        _drawerVisible.value = !_drawerVisible.value
    }

    fun showDrawer() {
        _drawerVisible.value = true
    }

    fun hideDrawer() {
        _drawerVisible.value = false
    }

    fun deleteChat(id: Int) {

    }

    fun deleteAllChat() {
        viewModelScope.launch(context = dispatcher) {
            repository.deleteAllChat()
        }
    }

    fun getGptResponse(userMessage: String) {
        viewModelScope.launch(context = dispatcher) {
            _chatScreenUiState.value = ChatScreenUiState(isLoading = true)
            val result = repository.getGptResponse(topic = userMessage)
            if (result.isSuccess && result.getOrNull() != null) {
                val fullResponse = requireNotNull(result.getOrNull()!!)
                _chatScreenUiState.value = ChatScreenUiState(
                    chat = Chat(
                        subject = userMessage,
                        aiAnswer = fullResponse.aiResponse,
                        timeStamp = fullResponse.timestamp
                    )
                )
                saveChat(
                    userMessage = userMessage,
                    aiResponse = fullResponse.aiResponse,
                    timeStamp = fullResponse.timestamp
                )
            } else {
                _chatScreenUiState.value = ChatScreenUiState(isError = true)
            }
        }
    }

    fun saveChat(userMessage: String, aiResponse: String, timeStamp: Long) {
        viewModelScope.launch(context = dispatcher) {
            _chatScreenUiState.value = ChatScreenUiState(isLoading = true)
            val chatHistory = ChatHistory(
                userMessage = userMessage,
                aiResponse = aiResponse,
                timestamp = timeStamp
            )
            repository.insertChat(chatHistory = chatHistory)
            _chatScreenUiState.value = ChatScreenUiState(
                chat = Chat(
                    subject = userMessage,
                    aiAnswer = aiResponse,
                    timeStamp = timeStamp
                )
            )
        }
    }
}