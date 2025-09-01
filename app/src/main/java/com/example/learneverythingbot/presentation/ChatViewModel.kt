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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    @DispatcherIO val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _chatScreenUiState = MutableStateFlow(ChatScreenUiState())
    val chatScreenUiState: StateFlow<ChatScreenUiState> = _chatScreenUiState.asStateFlow()

    private val _chatHistoryDrawerUiState = MutableStateFlow(ChatHistoryDrawerUiState())
    val chatHistoryDrawerUiState: StateFlow<ChatHistoryDrawerUiState> = _chatHistoryDrawerUiState.asStateFlow()

    private val _drawerVisible = MutableStateFlow(false)
    val drawerVisible: StateFlow<Boolean> = _drawerVisible.asStateFlow()

    private val _navigateToChatId = MutableStateFlow<Int?>(null)
    val navigateToChatId: StateFlow<Int?> = _navigateToChatId.asStateFlow()

    private val _currentChat = MutableStateFlow<ChatHistory?>(null)
    val currentChat: StateFlow<ChatHistory?> = _currentChat.asStateFlow()

    private val _isLoadingChat = MutableStateFlow(false)
    val isLoadingChat: StateFlow<Boolean> = _isLoadingChat.asStateFlow()

    init {
        loadChatHistory()
    }

    fun getGptResponse(topic: String) {
        viewModelScope.launch(dispatcher) {
            _chatScreenUiState.value = ChatScreenUiState(
                isLoading = true,
                error = null
            )

            val result = chatRepository.getGptResponse(topic)

            if (result.isSuccess) {
                val chatHistory = result.getOrThrow()
                // Insere o chat no banco (isso gerará um ID único)
                chatRepository.insertChat(chatHistory)

                // Recarrega o histórico para ter o ID correto
                loadChatHistory()

                _chatScreenUiState.value = ChatScreenUiState(
                    chat = Chat(
                        subject = chatHistory.userMessage,
                        aiAnswer = chatHistory.aiResponse,
                        timeStamp = chatHistory.timestamp
                    ),
                    isLoading = false
                )
            } else {
                _chatScreenUiState.value = ChatScreenUiState(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Erro desconhecido"
                )
            }
        }
    }

    fun loadChatHistory() {
        viewModelScope.launch(dispatcher) {
            try {
                chatRepository.getAllChatHistory().collect { chatHistoryList ->
                    _chatHistoryDrawerUiState.value = ChatHistoryDrawerUiState(chatHistoryList)
                }
            } catch (e: Exception) {
                println("Erro ao carregar histórico: ${e.message}")
            }
        }
    }

    fun loadChatById(id: Int) {
        viewModelScope.launch(dispatcher) {
            _isLoadingChat.value = true
            try {
                val chat = chatRepository.getChatById(id)
                _currentChat.value = chat
            } catch (e: Exception) {
                println("Erro ao carregar chat: ${e.message}")
            } finally {
                _isLoadingChat.value = false
            }
        }
    }

    fun showDrawer() {
        _drawerVisible.value = true
    }

    fun hideDrawer() {
        _drawerVisible.value = false
    }

    fun selectChat(chatHistory: ChatHistory) {
        _navigateToChatId.value = chatHistory.id
    }

    fun resetNavigation() {
        _navigateToChatId.value = null
        _currentChat.value = null
    }

    fun deleteChat(id: Int) {
        viewModelScope.launch(dispatcher) {
            chatRepository.deleteChat(id)
            loadChatHistory()

            if (_currentChat.value?.id == id) {
                _currentChat.value = null
            }
        }
    }

    fun deleteAllChat() {
        viewModelScope.launch(dispatcher) {
            chatRepository.deleteAllChat()
            loadChatHistory()
            _currentChat.value = null
        }
    }

    fun clearError() {
        _chatScreenUiState.value = _chatScreenUiState.value.copy(error = null)
    }
}